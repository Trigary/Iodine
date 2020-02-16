package hu.trigary.iodine.server.gui;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.container.RootGuiContainer;
import hu.trigary.iodine.server.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.NetworkManager;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * The implementation of {@link IodineRoot}, a base class for an actual implementation.
 *
 * @param <T> the class implementing this interface
 */
public abstract class IodineRootImpl<T extends IodineRoot<T>> implements IodineRoot<T>, GuiParentPlus<T> {
	private static final OutputBuffer BUFFER = new OutputBuffer();
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
	private boolean anythingFlagged;
	private WeakReference<byte[]> cachedOpenPacket;
	
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
		plugin.log(Level.OFF, "Root > adding element {0} to {1}", type, this.id);
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
		plugin.log(Level.OFF, "Root > removing element {0}", element == null ? "null" : element.getInternalId());
		if (element != null) {
			element.onRemoved();
			element.getParent().removeChild(element);
			elements.remove(element.getInternalId());
			flaggedForRemove.add(element);
			flagAndUpdate(null);
		}
		return thisT();
	}
	
	@NotNull
	@Override
	public final T removeElement(@NotNull GuiElement<?> element) {
		return removeElement(element.getId());
	}
	
	
	
	private void executeUpdate() {
		if (atomicUpdateLock != 0 || !anythingFlagged) {
			return;
		}
		
		plugin.log(Level.OFF, "Root > updating {0}", id);
		if (!viewers.isEmpty()) {
			flaggedForUpdate.removeAll(flaggedForRemove);
			serializeUpdateStart(BUFFER);
			byte[] payload = serialize(flaggedForRemove, flaggedForUpdate);
			NetworkManager network = plugin.getNetworkManager();
			for (IodinePlayerBase player : viewers) {
				network.send(player, payload);
			}
		}
		
		flaggedForUpdate.clear();
		flaggedForRemove.clear();
		anythingFlagged = false;
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
	 * Flags this root instance, and the specified element if it's not null, for update.
	 *
	 * @param element the element to flag
	 */
	public final void flagOnly(@Nullable GuiElementImpl<?> element) {
		if (element != null) {
			flaggedForUpdate.add(element);
		}
		anythingFlagged = true;
		cachedOpenPacket = null;
	}
	
	/**
	 * Flags this root instance, and the specified element if it's not null,
	 * for update and then also executes an update.
	 *
	 * @param element the element to flag
	 */
	public final void flagAndUpdate(@Nullable GuiElementImpl<?> element) {
		flagOnly(element);
		executeUpdate();
	}
	
	/**
	 * Flags this root instance, and the specified element if it's not null,
	 * for update and then executes an atomic update with the specified callback.
	 *
	 * @param element the element to flag
	 * @param updater the callback that updates this GUI
	 */
	public final void flagAndAtomicUpdate(@Nullable GuiElementImpl<?> element, @NotNull Runnable updater) {
		flagOnly(element);
		atomicUpdate(ignored -> updater.run());
	}
	
	
	
	@NotNull
	@Override
	public final T openFor(@NotNull IodinePlayer player) {
		IodinePlayerBase iodinePlayer = (IodinePlayerBase) player;
		iodinePlayer.assertModded();
		if (viewers.contains(player)) {
			plugin.log(Level.OFF, "Root > {0} already open for {1}", id, player.getName());
			return thisT();
		}
		
		plugin.log(Level.OFF, "Root > opening {0} for {1}", id, player.getName());
		onPreOpened(iodinePlayer);
		viewers.add(iodinePlayer);
		if (viewers.size() == 1) {
			plugin.getRootManager().rememberRoot(this);
		}
		
		onOpened(iodinePlayer);
		byte[] payload = cachedOpenPacket == null ? null : cachedOpenPacket.get();
		if (payload == null) {
			serializeOpenStart(BUFFER);
			payload = serialize(null, elements.values());
			cachedOpenPacket = new WeakReference<>(payload);
		}
		plugin.getNetworkManager().send(iodinePlayer, payload);
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
			plugin.log(Level.OFF, "Root > {0} already closed for {1}", id, iodinePlayer.getName());
		} else {
			plugin.log(Level.OFF, "Root > closing {0} for {1}", id, iodinePlayer.getName());
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
	
	
	
	/**
	 * Serializes the open packet's header into the specified buffer.
	 * Should only be called by {@link #IodineRootImpl(IodinePlugin, int)}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	protected abstract void serializeOpenStart(@NotNull OutputBuffer buffer);
	
	/**
	 * Serializes the update packet's header into the specified buffer.
	 * Should only be called by {@link #IodineRootImpl(IodinePlugin, int)}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	protected abstract void serializeUpdateStart(@NotNull OutputBuffer buffer);
	
	@NotNull
	private static byte[] serialize(@Nullable Collection<GuiElementImpl<?>> remove,
			@NotNull Collection<GuiElementImpl<?>> add) {
		if (remove != null) {
			BUFFER.putInt(remove.size());
			for (GuiElementImpl<?> element : remove) {
				BUFFER.putInt(element.getInternalId());
			}
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
