package hu.trigary.iodine.bukkit.gui.container.base;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.container.base.GuiBase;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.GuiBaseManager;
import hu.trigary.iodine.bukkit.gui.container.RootGuiContainer;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * The implementation of {@link GuiBase}.
 *
 * @param <T> the class implementing this interface
 */
public abstract class GuiBaseImpl<T extends GuiBase<T>> implements GuiBase<T>, GuiParentPlus<T> {
	private static final ResizingByteBuffer BUFFER = new ResizingByteBuffer(1000);
	private final Set<Player> viewers = new HashSet<>();
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
	 * Should only be called by {@link GuiBaseManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 */
	protected GuiBaseImpl(@NotNull IodinePlugin plugin, int id) {
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
	
	@Override
	public final void setAttachment(@Nullable Object attachment) {
		root.setAttachment(attachment);
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
	public final Set<Player> getViewers() {
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
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer, int x, int y) {
		Validate.notNull(id, "ID must be non-null");
		Validate.isTrue(!apiIdElements.containsKey(id), "IDs must be unique");
		plugin.log(Level.OFF, "GUI > adding element {0} to {1}", type, this.id);
		GuiElementImpl<E> impl = plugin.getGui().createElement(type.getType(), this, nextElementId, id);
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
		plugin.log(Level.OFF, "GUI > removing element {0}", element == null ? "null" : element.getInternalId());
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
	 * Updates this GUI for all of its viewers except in currently in an atomic update block.
	 */
	protected final void executeUpdate() {
		if (atomicUpdateLock != 0) {
			return;
		}
		
		plugin.log(Level.OFF, "GUI > updating {0}", id);
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
	public final T openFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		if (viewers.contains(player)) {
			plugin.log(Level.OFF, "GUI > {0} already open for {1}", id, player.getName());
			return thisT();
		}
		
		plugin.log(Level.OFF, "GUI > opening {0} for {1}", id, player.getName());
		onPreOpened(iodinePlayer);
		viewers.add(player);
		if (viewers.size() == 1) {
			plugin.getGui().rememberGui(this);
		}
		
		onOpened(iodinePlayer);
		plugin.getNetwork().send(player, serializeOpen());
		return thisT();
	}
	
	/**
	 * Called before this GUI has been opened for the specified player.
	 * Should be used to ensure that this GUI can opened without causing any issues.
	 *
	 * @param iodinePlayer the target player
	 */
	protected abstract void onPreOpened(@NotNull IodinePlayerImpl iodinePlayer);
	
	/**
	 * Called after this GUI has been opened for the specified player.
	 * Should be used to ensure that this GUI is listed in the {@link IodinePlayerImpl} instance.
	 *
	 * @param iodinePlayer the target player
	 */
	protected abstract void onOpened(@NotNull IodinePlayerImpl iodinePlayer);
	
	@NotNull
	@Override
	public final T closeFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer, false);
		sendClosePacket(plugin.getNetwork(), player);
		return thisT();
	}
	
	/**
	 * Sends a packet that informs the player that it should close this GUI.
	 *
	 * @param network the network manager instance
	 * @param player the target player
	 */
	protected abstract void sendClosePacket(@NotNull NetworkManager network, @NotNull Player player);
	
	/**
	 * Closes this GUI for the specified player.
	 * This method differs from {@link #closeFor(Player)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 * @param byPlayer whether the player or a server-side method closed the GUI
	 */
	public final void closeForNoPacket(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer) {
		Player player = iodinePlayer.getPlayer();
		if (!viewers.remove(player)) {
			plugin.log(Level.OFF, "GUI > {0} already closed for {1}", id, player.getName());
		} else {
			plugin.log(Level.OFF, "GUI > closing {0} for {1}", id, player.getName());
			if (viewers.isEmpty()) {
				plugin.getGui().forgetGui(this);
			}
			onClosed(iodinePlayer, byPlayer);
		}
	}
	
	/**
	 * Called after this GUI has been closed for the specified player.
	 * Should be used to ensure that this GUI is no longer listed in the {@link IodinePlayerImpl} instance.
	 *
	 * @param iodinePlayer the target player
	 * @param byPlayer whether the player or a server-side method closed the GUI
	 */
	protected abstract void onClosed(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer);
	
	
	
	@NotNull
	private byte[] serializeOpen() {
		serializeOpenStart(BUFFER);
		return serialize(Collections.emptyList(), elements.values());
	}
	
	protected abstract void serializeOpenStart(@NotNull ResizingByteBuffer buffer);
	
	@NotNull
	private byte[] serializeUpdate() {
		serializeUpdateStart(BUFFER);
		return serialize(flaggedForRemove, flaggedForUpdate);
	}
	
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
}
