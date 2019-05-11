package hu.trigary.iodine.bukkit.api.gui;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.GuiElement;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.GuiManager;
import hu.trigary.iodine.bukkit.api.gui.element.GuiElementImpl;
import hu.trigary.iodine.bukkit.api.player.IodinePlayerImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The implementation of {@link IodineGui}.
 */
public class IodineGuiImpl extends IodineGui {
	private static final ByteBuffer BUFFER = ByteBuffer.wrap(new byte[0xFFF]);
	//these seem like reasonable values: probably max 2 GUIs being
	//simultaneously edited and a GUI surely fits into 4095 bytes
	
	private final Set<Player> viewers = new HashSet<>();
	private final Map<Object, GuiElementImpl> elements = new HashMap<>();
	private final IodinePlugin plugin;
	private final int id;
	private int nextElementId;
	private int atomicUpdateLock;
	private BiConsumer<IodineGui, Player> openAction;
	private BiConsumer<IodineGui, Player> closeAction;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 */
	public IodineGuiImpl(@NotNull IodinePlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	
	
	/**
	 * Gets this GUI's unique identifier.
	 *
	 * @return the ID of this GUI
	 */
	@Contract(pure = true)
	public int getId() {
		return id;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Collection<Player> getViewers() {
		return Collections.unmodifiableCollection(viewers);
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public GuiElement getElement(@NotNull Object id) {
		Validate.notNull(id, "IDs must be non-null");
		return elements.get(id);
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public Collection<GuiElement> getAllElements() {
		return Collections.unmodifiableCollection(elements.values());
	}
	
	
	
	@NotNull
	@Override
	public IodineGui setOpenAction(@Nullable BiConsumer<IodineGui, Player> action) {
		openAction = action;
		return this;
	}
	
	@NotNull
	@Override
	public IodineGui setCloseAction(@Nullable BiConsumer<IodineGui, Player> action) {
		closeAction = action;
		return this;
	}
	
	
	
	@NotNull
	@Override
	public <T extends GuiElement> IodineGui addElement(@NotNull Object id,
			@NotNull Class<T> type, @NotNull Consumer<T> initializer) {
		Validate.notNull(id, "IDs must be non-null");
		Validate.isTrue(!elements.containsKey(id), "IDs must be unique");
		GuiElementImpl element = plugin.getGui().createElement(type, this, nextElementId++, id);
		elements.put(id, element);
		atomicUpdate(gui -> initializer.accept(type.cast(element)));
		return this;
	}
	
	@NotNull
	@Override
	public IodineGui removeElement(@NotNull Object id) {
		Validate.notNull(id, "IDs must be non-null");
		GuiElement element = elements.remove(id);
		if (element != null) {
			update();
		}
		return this;
	}
	
	
	
	@NotNull
	@Override
	public IodineGui atomicUpdate(@NotNull Consumer<IodineGui> updater) {
		atomicUpdateLock++;
		updater.accept(this);
		atomicUpdateLock--;
		update();
		return this;
	}
	
	/**
	 * Updates this GUI for all of its viewers if
	 * there is no atomic update in progress.
	 * Otherwise does nothing.
	 */
	public void update() {
		if (atomicUpdateLock == 0 && !viewers.isEmpty()) {
			byte[] payload = serialize(PacketType.SERVER_GUI_CHANGE);
			NetworkManager network = plugin.getNetwork();
			viewers.forEach(player -> network.send(player, payload));
		}
	}
	
	
	
	@NotNull
	@Override
	public IodineGui openFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		
		IodineGuiImpl previous = iodinePlayer.getOpenGui();
		if (previous == this) {
			return this;
		} else if (previous != null) {
			previous.closeForNoPacket(iodinePlayer);
		}
		
		viewers.add(player);
		if (viewers.size() == 1) {
			plugin.getGui().rememberGui(this);
		}
		
		iodinePlayer.setOpenGui(this);
		if (openAction != null) {
			openAction.accept(this, player);
		}
		
		plugin.getNetwork().send(player, serialize(PacketType.SERVER_GUI_OPEN));
		return this;
	}
	
	@NotNull
	@Override
	public IodineGui closeFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer);
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_CLOSE, 4, buffer -> buffer.putInt(id));
		return this;
	}
	
	/**
	 * Closes this GUI for the specified player.
	 * This method differs from {@link #closeFor(Player)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 */
	public void closeForNoPacket(@NotNull IodinePlayerImpl iodinePlayer) {
		Player player = iodinePlayer.getPlayer();
		if (!viewers.remove(player)) {
			return;
		}
		
		if (viewers.isEmpty()) {
			plugin.getGui().forgetGui(this);
		}
		
		iodinePlayer.setOpenGui(null);
		if (closeAction != null) {
			closeAction.accept(this, player);
		}
	}
	
	
	
	private byte[] serialize(PacketType packetType) {
		BUFFER.put(packetType.getId());
		BUFFER.putInt(id);
		elements.values().forEach(element -> element.serialize(BUFFER));
		byte[] result = new byte[BUFFER.flip().remaining()];
		BUFFER.get(result).rewind();
		return result;
	}
}
