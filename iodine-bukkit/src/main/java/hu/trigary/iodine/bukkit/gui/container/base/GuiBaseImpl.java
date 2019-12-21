package hu.trigary.iodine.bukkit.gui.container.base;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.container.base.GuiBase;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
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

/**
 * The implementation of {@link GuiBase}.
 *
 * @param <T> the class implementing this interface
 */
public abstract class GuiBaseImpl<T extends GuiBase<T>> implements GuiBase<T>, GuiParentPlus<T> {
	private static final ResizingByteBuffer BUFFER = new ResizingByteBuffer(1000);
	private final Set<Player> viewers = new HashSet<>();
	private final Map<Short, GuiElementImpl<?>> elements = new HashMap<>();
	private final Map<Object, GuiElementImpl<?>> apiIdElements = new HashMap<>();
	private final Set<GuiElementImpl<?>> flaggedForUpdate = new HashSet<>();
	private final Set<GuiElementImpl<?>> flaggedForRemove = new HashSet<>();
	private final IodinePlugin plugin;
	private final int id;
	private final RootGuiContainer root;
	private short nextElementId = 1;
	private int atomicUpdateLock;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link hu.trigary.iodine.bukkit.gui.GuiBaseManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 */
	protected GuiBaseImpl(@NotNull IodinePlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
		root = new RootGuiContainer(this);
		elements.put((short) 0, root);
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
	public final GuiElementImpl<?> getElement(short id) {
		return elements.get(id);
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public final GuiElementImpl<?> getElement(@NotNull Object id) {
		Validate.notNull(id, "ID must be non-null");
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
		Validate.notNull(id, "ID must be non-null");
		GuiElementImpl<?> element = apiIdElements.remove(id);
		if (element != null) {
			elements.remove(element.getInternalId());
			flaggedForRemove.add(element);
			executeUpdate();
		}
		return thisT();
	}
	
	@NotNull
	@Override
	public final T removeElement(@NotNull GuiElement<?> element) {
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
			return thisT();
		}
		
		onPreOpened(iodinePlayer);
		viewers.add(player);
		if (viewers.size() == 1) {
			plugin.getGui().rememberGui(this);
		}
		
		onOpened(iodinePlayer);
		plugin.getNetwork().send(player, serializeOpen());
		return thisT();
	}
	
	//TODO docs
	protected abstract void onPreOpened(@NotNull IodinePlayerImpl iodinePlayer);
	
	//TODO docs
	protected abstract void onOpened(@NotNull IodinePlayerImpl iodinePlayer);
	
	@NotNull
	@Override
	public final T closeFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer, false);
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_OVERLAY_CLOSE);
		return thisT();
	}
	
	/**
	 * Closes this GUI for the specified player.
	 * This method differs from {@link #closeFor(Player)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 */
	public final void closeForNoPacket(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer) {
		Player player = iodinePlayer.getPlayer();
		if (viewers.remove(player)) {
			if (viewers.isEmpty()) {
				plugin.getGui().forgetGui(this);
			}
			onClosed(iodinePlayer, byPlayer);
		}
	}
	
	//TODO docs
	protected abstract void onClosed(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer);
	
	
	
	@NotNull
	private byte[] serializeOpen() {
		serializeOpenStart(BUFFER);
		return serialize(Collections.emptyList(), elements.values());
	}
	
	//TODO docs
	protected abstract void serializeOpenStart(@NotNull ResizingByteBuffer buffer);
	
	@NotNull
	private byte[] serializeUpdate() {
		serializeUpdateStart(BUFFER);
		return serialize(flaggedForRemove, flaggedForUpdate);
	}
	
	//TODO docs
	protected abstract void serializeUpdateStart(@NotNull ResizingByteBuffer buffer);
	
	@NotNull
	private static byte[] serialize(@NotNull Collection<GuiElementImpl<?>> remove,
			@NotNull Collection<GuiElementImpl<?>> add) {
		BUFFER.putInt(remove.size());
		for (GuiElementImpl<?> element : remove) {
			BUFFER.putShort(element.getInternalId());
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
