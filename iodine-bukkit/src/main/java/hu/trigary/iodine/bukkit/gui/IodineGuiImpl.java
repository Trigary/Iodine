package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.GuiParentPlus;
import hu.trigary.iodine.bukkit.gui.element.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;

/**
 * The implementation of {@link IodineGui}.
 */
public class IodineGuiImpl implements IodineGui, GuiParentPlus<IodineGui> {
	private static final ByteBuffer BUFFER = ByteBuffer.wrap(new byte[0xFFF]); //a GUI surely fits in ~4kB
	private final Set<Player> viewers = new HashSet<>();
	private final Map<Integer, GuiElementImpl<?>> elements = new HashMap<>();
	private final Map<Object, GuiElementImpl<?>> apiIdElements = new HashMap<>();
	private final Map<GuiElementImpl<?>, Position> children = new HashMap<>();
	private final IodinePlugin plugin;
	private final int id;
	private int nextElementId;
	private int atomicUpdateLock;
	private ClosedAction closedAction;
	
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
	public GuiElementImpl<?> getElement(@NotNull Object id) {
		Validate.notNull(id, "IDs must be non-null");
		return apiIdElements.get(id);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Collection<GuiElement<?>> getAllElements() {
		return Collections.unmodifiableCollection(elements.values());
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Collection<GuiElement<?>> getChildren() {
		return Collections.unmodifiableCollection(children.keySet());
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		Validate.isTrue(x >= 0 && y >= 0 && x <= Short.MAX_VALUE && y <= Short.MAX_VALUE,
				"The element's render position must be at least 0 and at most Short.MAX_VALUE");
		//TODO can it fit into 8 bits instead?
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		Validate.isTrue(children.put(impl, new Position((short) x, (short) y)) == null,
				"The specified element is already the child of this GUI");
		impl.setParent(this);
		update();
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		Validate.notNull(children.remove(child),
				"The specified element is not a child of this parent");
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> IodineGui addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer, int x, int y) {
		Validate.notNull(id, "IDs must be non-null");
		Validate.isTrue(!apiIdElements.containsKey(id), "IDs must be unique");
		GuiElementImpl<E> impl = plugin.getGui().createElement(type.getType(), this, nextElementId, id);
		elements.put(nextElementId++, impl);
		apiIdElements.put(id, impl);
		atomicUpdate(gui -> {
			//noinspection unchecked
			E element = (E) impl;
			makeChild(element, x, y);
			initializer.accept(element);
		});
		return this;
	}
	
	@NotNull
	@Override
	public IodineGuiImpl removeElement(@NotNull Object id) {
		Validate.notNull(id, "IDs must be non-null");
		GuiElementImpl<?> element = apiIdElements.remove(id);
		if (element != null) {
			update();
		}
		return this;
	}
	
	
	
	@NotNull
	@Override
	public IodineGuiImpl atomicUpdate(@NotNull Consumer<IodineGui> updater) {
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
			byte[] payload = serialize(false);
			NetworkManager network = plugin.getNetwork();
			for (Player player : viewers) {
				network.send(player, payload);
			}
		}
	}
	
	
	
	@NotNull
	@Override
	public IodineGuiImpl openFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		
		IodineGuiImpl previous = iodinePlayer.getOpenGui();
		if (previous == this) {
			return this;
		} else if (previous != null) {
			previous.closeForNoPacket(iodinePlayer, false);
		}
		
		viewers.add(player);
		if (viewers.size() == 1) {
			plugin.getGui().rememberGui(this);
		}
		
		iodinePlayer.setOpenGui(this);
		plugin.getNetwork().send(player, serialize(true));
		return this;
	}
	
	@NotNull
	@Override
	public IodineGuiImpl closeFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer, false);
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_CLOSE);
		return this;
	}
	
	/**
	 * Closes this GUI for the specified player.
	 * This method differs from {@link #closeFor(Player)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 */
	public void closeForNoPacket(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer) {
		Player player = iodinePlayer.getPlayer();
		if (!viewers.remove(player)) {
			return;
		}
		
		if (viewers.isEmpty()) {
			plugin.getGui().forgetGui(this);
		}
		
		iodinePlayer.setOpenGui(null);
		if (byPlayer && closedAction != null) {
			closedAction.accept(this, player);
		}
	}
	
	@NotNull
	@Override
	public IodineGuiImpl onClosed(@Nullable ClosedAction action) {
		closedAction = action;
		return this;
	}
	
	
	
	private byte[] serialize(boolean opening) {
		if (opening) {
			BUFFER.put(PacketType.SERVER_GUI_OPEN.getId());
			BUFFER.putInt(id);
		} else {
			BUFFER.put(PacketType.SERVER_GUI_CHANGE.getId());
		}
		
		for (GuiElementImpl<?> element : elements.values()) {
			element.serialize(BUFFER);
		}
		
		children.forEach((element, position) -> {
			BUFFER.putInt(element.getInternalId());
			BUFFER.putShort(position.x);
			BUFFER.putShort(position.y);
		});
		
		byte[] result = new byte[BUFFER.flip().remaining()];
		BUFFER.get(result).rewind();
		return result;
	}
	
	
	
	private static class Position {
		final short x;
		final short y;
		
		Position(short x, short y) {
			this.x = x;
			this.y = y;
		}
	}
}
