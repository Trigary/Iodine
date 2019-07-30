package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.RootGuiContainer;
import hu.trigary.iodine.bukkit.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
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
	private final Set<GuiElementImpl<?>> flaggedForUpdate = new HashSet<>();
	private final Set<GuiElementImpl<?>> flaggedForRemove = new HashSet<>();
	private final IodinePlugin plugin;
	private final int id;
	private final RootGuiContainer root;
	private int nextElementId = 1;
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
		root = new RootGuiContainer(this);
		elements.put(0, root);
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
	public Set<Player> getViewers() {
		return Collections.unmodifiableSet(viewers);
	}
	
	/**
	 * Gets the element which has the specified internal ID.
	 * Returns null if no matching elements were found.
	 *
	 * @param id the ID to search for
	 * @return the matching element or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	public GuiElementImpl<?> getElement(int id) {
		return elements.get(id);
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public GuiElementImpl<?> getElement(@NotNull Object id) {
		Validate.notNull(id, "ID must be non-null");
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
		return root.getChildren();
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		return root.makeChild(element, x, y);
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		root.removeChild(child);
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> IodineGui addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer, int x, int y) {
		Validate.notNull(id, "ID must be non-null");
		Validate.isTrue(!apiIdElements.containsKey(id), "IDs must be unique");
		GuiElementImpl<E> impl = plugin.getGui().createElement(type.getType(), this, nextElementId, id);
		elements.put(nextElementId++, impl);
		apiIdElements.put(id, impl);
		flagAndAtomicUpdate(impl, () -> {
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
		Validate.notNull(id, "ID must be non-null");
		GuiElementImpl<?> element = apiIdElements.remove(id);
		if (element != null) {
			elements.remove(element.getInternalId());
			flaggedForRemove.add(element);
			executeUpdate();
		}
		return this;
	}
	
	@NotNull
	@Override
	public IodineGuiImpl removeElement(@NotNull GuiElement<?> element) {
		Validate.notNull(element, "Element must be non-null");
		return removeElement(element.getId());
	}
	
	
	
	private void executeUpdate() {
		if (atomicUpdateLock != 0) {
			return;
		}
		
		if (!viewers.isEmpty()) {
			flaggedForUpdate.removeAll(flaggedForRemove);
			byte[] payload = serializeUpdate();
			NetworkManager network = plugin.getNetwork();
			for (Player player : viewers) {
				network.send(player, payload);
			}
		}
		
		flaggedForUpdate.clear();
		flaggedForRemove.clear();
	}
	
	@NotNull
	@Override
	public IodineGuiImpl atomicUpdate(@NotNull Consumer<IodineGui> updater) {
		atomicUpdateLock++;
		updater.accept(this);
		atomicUpdateLock--;
		executeUpdate();
		return this;
	}
	
	
	
	/**
	 * Flags the specified element for update.
	 * No errors are thrown, even if the element has already been flagged or removed.
	 *
	 * @param element the element to flag
	 */
	public void flagOnly(@NotNull GuiElementImpl<?> element) {
		flaggedForUpdate.add(element);
	}
	
	/**
	 * Flags the specified element for update and then executes an update.
	 * No errors are thrown, even if the element has already been flagged or removed.
	 *
	 * @param element the element to flag
	 */
	public void flagAndUpdate(@NotNull GuiElementImpl<?> element) {
		flagOnly(element);
		executeUpdate();
	}
	
	/**
	 * Flags the specified element for update and then executes an atomic update with the specified callback.
	 * No errors are thrown, even if the element has already been flagged or removed.
	 *
	 * @param element the element to flag
	 * @param updater the callback that updates this GUI
	 */
	public void flagAndAtomicUpdate(@NotNull GuiElementImpl<?> element, @NotNull Runnable updater) {
		flagOnly(element);
		atomicUpdate(ignored -> updater.run());
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
		plugin.getNetwork().send(player, serializeOpen());
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
	
	
	
	private byte[] serializeOpen() {
		BUFFER.put(PacketType.SERVER_GUI_OPEN.getId());
		BUFFER.putInt(id);
		return serialize(Collections.emptyList(), elements.values());
	}
	
	private byte[] serializeUpdate() {
		BUFFER.put(PacketType.SERVER_GUI_CHANGE.getId());
		return serialize(flaggedForRemove, flaggedForUpdate);
	}
	
	private static byte[] serialize(@NotNull Collection<GuiElementImpl<?>> remove,
			@NotNull Collection<GuiElementImpl<?>> add) {
		BUFFER.putInt(remove.size());
		for (GuiElementImpl<?> element : remove) {
			BUFFER.putInt(element.getInternalId());
		}
		
		for (GuiElementImpl<?> element : add) {
			element.serialize(BUFFER);
		}
		
		byte[] result = new byte[BUFFER.flip().remaining()];
		BUFFER.get(result).clear();
		return result;
	}
}
