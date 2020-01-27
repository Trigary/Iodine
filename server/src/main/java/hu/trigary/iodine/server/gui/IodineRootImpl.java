package hu.trigary.iodine.server.gui;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.container.RootGuiContainer;
import hu.trigary.iodine.server.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.NetworkManager;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * The implementation of {@link IodineRoot}, a base class for an actual implementation.
 *
 * @param <T> the class implementing this interface
 */
public abstract class IodineRootImpl<T extends IodineRoot<T>> implements IodineRoot<T>, GuiParentPlus<T> {
	private static final ResizingByteBuffer BUFFER = new ResizingByteBuffer(1000);
	private final Set<IodinePlayerBase> viewers = new HashSet<>();
	private final Map<Integer, GuiElementImpl<?>> elements = new HashMap<>();
	private final Map<Object, GuiElementImpl<?>> apiIdElements = new HashMap<>();
	private final Set<GuiElementImpl<?>> flaggedForUpdate = new HashSet<>();
	private final Set<GuiElementImpl<?>> flaggedForRemove = new HashSet<>();
	private final IodinePlugin plugin;
	private final int id;
	private final RootGuiContainer root;
	private int nextElementId = 1; //id 0 is root
	private int atomicUpdateLock;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link IodineRootManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this instance
	 */
	protected IodineRootImpl(@NotNull IodinePlugin plugin, int id) {
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
	public final int getId() {
		return id;
	}
	
	@NotNull
	@Override
	public final T setAttachment(@Nullable Object attachment) {
		root.setAttachment(attachment);
		return thisT();
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public final Object getAttachment() {
		return root.getAttachment();
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Set<IodinePlayer> getViewers() {
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
	public final GuiElementImpl<?> getElement(int id) {
		return elements.get(id);
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public final GuiElementImpl<?> getElement(@NotNull Object id) {
		return apiIdElements.get(id);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Collection<GuiElement<?>> getAllElements() {
		return Collections.unmodifiableCollection(elements.values());
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Collection<GuiElement<?>> getChildren() {
		return root.getChildren();
	}
	
	
	
	@NotNull
	@Override
	public final <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		return root.makeChild(element, x, y);
	}
	
	@Override
	public final void removeChild(@NotNull GuiElementImpl<?> child) {
		root.removeChild(child);
	}
	
	
	
	@NotNull
	@Override
	public final <E extends GuiElement<E>> T addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, int x, int y, @NotNull Consumer<E> initializer) {
		Validate.notNull(id, "ID must be non-null");
		Validate.isTrue(!apiIdElements.containsKey(id), "IDs must be unique");
		plugin.log(Level.OFF, "Base > adding element {0} to {1}", type, this.id);
		GuiElementImpl<E> impl = plugin.getRootManager().createElement(type.getType(), this, nextElementId, id);
		elements.put(nextElementId++, impl);
		apiIdElements.put(id, impl);
		flagAndAtomicUpdate(impl, () -> {
			//noinspection unchecked
			E element = (E) impl;
			makeChild(element, x, y);
			initializer.accept(element);
		});
		return thisT();
	}
	
	@NotNull
	@Override
	public final T removeElement(@NotNull Object id) {
		GuiElementImpl<?> element = apiIdElements.remove(id);
		plugin.log(Level.OFF, "Base > removing element {0}", element == null ? "null" : element.getInternalId());
		if (element != null) {
			element.onRemoved();
			element.getParent().removeChild(element);
			elements.remove(element.getInternalId());
			flaggedForRemove.add(element);
			executeUpdate();
		}
		return thisT();
	}
	
	@NotNull
	@Override
	public final T removeElement(@NotNull GuiElement<?> element) {
		return removeElement(element.getId());
	}
	
	
	
	/**
	 * Updates this instance for all of its viewers except in currently in an atomic update block.
	 */
	protected final void executeUpdate() {
		if (atomicUpdateLock != 0) {
			return;
		}
		
		plugin.log(Level.OFF, "Base > updating {0}", id);
		if (!viewers.isEmpty()) {
			flaggedForUpdate.removeAll(flaggedForRemove);
			byte[] payload = serializeUpdate();
			NetworkManager network = plugin.getNetworkManager();
			for (IodinePlayerBase player : viewers) {
				network.send(player, payload);
			}
		}
		
		flaggedForUpdate.clear();
		flaggedForRemove.clear();
	}
	
	@NotNull
	@Override
	public final T atomicUpdate(@NotNull Consumer<T> updater) {
		atomicUpdateLock++;
		updater.accept(thisT());
		atomicUpdateLock--;
		executeUpdate();
		return thisT();
	}
	
	
	
	/**
	 * Flags the specified element for update.
	 * No errors are thrown, even if the element has already been flagged or removed.
	 *
	 * @param element the element to flag
	 */
	public final void flagOnly(@NotNull GuiElementImpl<?> element) {
		flaggedForUpdate.add(element);
	}
	
	/**
	 * Flags the specified element for update and then executes an update.
	 * No errors are thrown, even if the element has already been flagged or removed.
	 *
	 * @param element the element to flag
	 */
	public final void flagAndUpdate(@NotNull GuiElementImpl<?> element) {
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
	public final void flagAndAtomicUpdate(@NotNull GuiElementImpl<?> element, @NotNull Runnable updater) {
		flagOnly(element);
		atomicUpdate(ignored -> updater.run());
	}
	
	
	
	@NotNull
	@Override
	public final T openFor(@NotNull IodinePlayer player) {
		IodinePlayerBase iodinePlayer = (IodinePlayerBase) player;
		iodinePlayer.assertModded();
		if (viewers.contains(player)) {
			plugin.log(Level.OFF, "Base > {0} already open for {1}", id, player.getName());
			return thisT();
		}
		
		plugin.log(Level.OFF, "Base > opening {0} for {1}", id, player.getName());
		onPreOpened(iodinePlayer);
		viewers.add(iodinePlayer);
		if (viewers.size() == 1) {
			plugin.getRootManager().rememberRoot(this);
		}
		
		onOpened(iodinePlayer);
		plugin.getNetworkManager().send(iodinePlayer, serializeOpen());
		return thisT();
	}
	
	/**
	 * Called before this instance has been opened for the specified player.
	 * Should be used to ensure that this instance can opened without causing any issues.
	 *
	 * @param player the target player
	 */
	protected abstract void onPreOpened(@NotNull IodinePlayerBase player);
	
	/**
	 * Called after this instance has been opened for the specified player.
	 * Should be used to ensure that this instance is listed in the {@link IodinePlayerBase} instance.
	 *
	 * @param player the target player
	 */
	protected abstract void onOpened(@NotNull IodinePlayerBase player);
	
	@NotNull
	@Override
	public final T closeFor(@NotNull IodinePlayer player) {
		IodinePlayerBase iodinePlayer = (IodinePlayerBase) player;
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer, false);
		sendClosePacket(plugin.getNetworkManager(), iodinePlayer);
		return thisT();
	}
	
	/**
	 * Sends a packet that informs the player that it should close this GUI.
	 *
	 * @param network the network manager instance
	 * @param player the target player
	 */
	protected abstract void sendClosePacket(@NotNull NetworkManager network, @NotNull IodinePlayerBase player);
	
	/**
	 * Closes this instance for the specified player.
	 * This method differs from {@link #closeFor(IodinePlayer)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 * @param byPlayer whether the player or a server-side method closed the GUI
	 */
	public final void closeForNoPacket(@NotNull IodinePlayerBase iodinePlayer, boolean byPlayer) {
		if (!viewers.remove(iodinePlayer)) {
			plugin.log(Level.OFF, "Base > {0} already closed for {1}", id, iodinePlayer.getName());
		} else {
			plugin.log(Level.OFF, "Base > closing {0} for {1}", id, iodinePlayer.getName());
			if (viewers.isEmpty()) {
				plugin.getRootManager().forgetRoot(this);
			}
			onClosed(iodinePlayer, byPlayer);
		}
	}
	
	/**
	 * Called after this instance has been closed for the specified player.
	 * Should be used to ensure that this instance is no longer listed in the {@link IodinePlayerBase} instance.
	 *
	 * @param iodinePlayer the target player
	 * @param byPlayer whether the player or a server-side method closed the GUI
	 */
	protected abstract void onClosed(@NotNull IodinePlayerBase iodinePlayer, boolean byPlayer);
	
	
	
	@NotNull
	private byte[] serializeOpen() {
		serializeOpenStart(BUFFER);
		return serialize(Collections.emptyList(), elements.values());
	}
	
	/**
	 * Serializes the open packet's header into the specified buffer.
	 * Should only be called by {@link #IodineRootImpl(IodinePlugin, int)}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	protected abstract void serializeOpenStart(@NotNull ResizingByteBuffer buffer);
	
	@NotNull
	private byte[] serializeUpdate() {
		serializeUpdateStart(BUFFER);
		return serialize(flaggedForRemove, flaggedForUpdate);
	}
	
	/**
	 * Serializes the update packet's header into the specified buffer.
	 * Should only be called by {@link #IodineRootImpl(IodinePlugin, int)}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	protected abstract void serializeUpdateStart(@NotNull ResizingByteBuffer buffer);
	
	@NotNull
	private static byte[] serialize(@NotNull Collection<GuiElementImpl<?>> remove,
			@NotNull Collection<GuiElementImpl<?>> add) {
		BUFFER.putInt(remove.size());
		for (GuiElementImpl<?> element : remove) {
			BUFFER.putInt(element.getInternalId());
		}
		for (GuiElementImpl<?> element : add) {
			element.serialize(BUFFER);
		}
		return BUFFER.toArrayAndReset();
	}
	
	@NotNull
	@Contract(pure = true)
	private T thisT() {
		//noinspection unchecked
		return (T) this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getId();
	}
}
