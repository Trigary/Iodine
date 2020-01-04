package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.gui.container.base.GuiParent;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents a {@link IodineGui} or a {@link IodineOverlay}.
 * Instances of this class are the top-level roots of GUIs/overlays.
 *
 * @param <T> the class implementing this interface
 */
public interface IodineRoot<T extends IodineRoot<T>> extends GuiParent<T>, AttachmentHolder<T> {
	/**
	 * Gets the players who have this instance opened.
	 * The returned set is an unmodifiable view of the underlying data.
	 *
	 * @return the players who have this instance opened
	 */
	@NotNull
	@Contract(pure = true)
	Set<IodinePlayer> getViewers();
	
	/**
	 * Gets the element which has the specified ID.
	 * Returns null if no matching elements were found.
	 *
	 * @param id the ID to search for
	 * @return the matching element or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	GuiElement<?> getElement(@NotNull Object id);
	
	/**
	 * Gets the elements that have been added to this instance.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the elements this instance contains
	 */
	@NotNull
	@Contract(pure = true)
	Collection<GuiElement<?>> getAllElements();
	
	
	
	/**
	 * Makes the specified element a direct child of this instance.
	 *
	 * @param element the element to add as a child
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @param <E> the type of the element
	 * @return the current instance (for chaining)
	 */
	@NotNull <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y);
	
	
	
	/**
	 * Adds a new element of the specified type to this instance.
	 * The parent of the element will be this instance.
	 * The initializer function atomically initializes the new element.
	 * The element will be accessible using the specified ID.
	 * This ID must be unique.
	 * If ID-based access is not required, then optionally
	 * {@link #addElement(GuiElements, int, int, Consumer)} can be used instead.
	 *
	 * @param id id the identifier of the element which can be used later to retrieve it
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull <E extends GuiElement<E>> T addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, int x, int y, @NotNull Consumer<E> initializer);
	
	/**
	 * Adds a new element of the specified type to this instance.
	 * Same as calling {@link #addElement(Object, GuiElements, int, int, Consumer)}
	 * with {@code new Object()} as the ID.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> T addElement(@NotNull GuiElements<E> type,
			int x, int y, @NotNull Consumer<E> initializer) {
		return addElement(new Object(), type, x, y, initializer);
	}
	
	/**
	 * Adds a new element of the specified type to this instance.
	 * Same as calling {@link #addElement(Object, GuiElements, int, int, Consumer)}
	 * with 0;0 as the X;Y positions.
	 *
	 * @param id id the identifier of the element which can be used later to retrieve it
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> T addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer) {
		return addElement(id, type, 0, 0, initializer);
	}
	
	/**
	 * Adds a new element of the specified type to this instance.
	 * Same as calling {@link #addElement(Object, GuiElements, int, int, Consumer)}
	 * with {@code new Object()} as the ID and 0;0 as the X;Y positions.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> T addElement(@NotNull GuiElements<E> type,
			@NotNull Consumer<E> initializer) {
		return addElement(new Object(), type, 0, 0, initializer);
	}
	
	
	
	/**
	 * Deletes the element with the specified ID from this instance.
	 * If no element is found with that ID, then this method does nothing.
	 *
	 * @param id the identifier of the element
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T removeElement(@NotNull Object id);
	
	/**
	 * Deletes the specified element from this instance.
	 * If the element is not part of this instance, then this method does nothing.
	 *
	 * @param element the element to remove
	 * @return the current instance (for chaining)
	 */
	T removeElement(@NotNull GuiElement<?> element);
	
	
	
	/**
	 * Runs the specified callback, executing all updates atomically.
	 * This means that the instance won't be updated for the players
	 * until the callback has returned.
	 * This way the players can't observe intermediate states,
	 * only start and result states.
	 *
	 * @param updater the callback that updates this instance
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T atomicUpdate(@NotNull Consumer<T> updater);
	
	
	
	/**
	 * Opens this instance for the specified player.
	 * This method will do nothing if this instance is already opened for the player.
	 * If the player has a instance opened that it is not this one, then that instance will be closed.
	 * This operation is only valid for modded players.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T openFor(@NotNull IodinePlayer player);
	
	/**
	 * Closes this instance for the specified player.
	 * This method will do nothing if this instance is not opened for the player.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T closeFor(@NotNull IodinePlayer player);
}
