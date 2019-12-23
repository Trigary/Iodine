package hu.trigary.iodine.api.gui.container.base;

import hu.trigary.iodine.api.gui.AttachmentHolder;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents a {@link IodineGui} or a {@link IodineOverlay}.
 * Instances of this class are the top-level roots of GUIs.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiBase<T extends GuiBase<T>> extends GuiParent<T>, AttachmentHolder {
	/**
	 * The inclusive lower bound for element coordinates.
	 */
	int COORDINATE_LOWER_BOUND = Short.MIN_VALUE;
	
	/**
	 * The inclusive upper bound for element coordinates.
	 */
	int COORDINATE_UPPER_BOUND = Short.MAX_VALUE;
	
	/**
	 * Gets the players who have this GUI opened.
	 * The returned set is an unmodifiable view of the underlying data.
	 *
	 * @return the players who have this GUI opened
	 */
	@NotNull
	@Contract(pure = true)
	Set<Player> getViewers();
	
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
	 * Gets the elements that have been added to this GUI.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the elements this GUI contains
	 */
	@NotNull
	@Contract(pure = true)
	Collection<GuiElement<?>> getAllElements();
	
	
	
	/**
	 * Makes the specified element a direct child of this GUI.
	 * The element's coordinates must be at least {@link #COORDINATE_LOWER_BOUND}
	 * and at most {@link #COORDINATE_UPPER_BOUND}.
	 *
	 * @param element the element to add as a child
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @return the current instance (for chaining)
	 */
	@NotNull <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y);
	
	
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * The parent of the element will be this GUI.
	 * The initializer function atomically initializes the new element.
	 * The element will be accessible using the specified ID.
	 * This ID must be unique.
	 * The element's coordinates must be at least {@link #COORDINATE_LOWER_BOUND}
	 * and at most {@link #COORDINATE_UPPER_BOUND}.
	 * If ID-based access is not required, then optionally
	 * {@link #addElement(GuiElements, Consumer, int, int)} can be used instead.
	 *
	 * @param id id the identifier of the element
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull <E extends GuiElement<E>> T addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer, int x, int y);
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * Same as calling {@link #addElement(Object, GuiElements, Consumer, int, int)}
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
			@NotNull Consumer<E> initializer, int x, int y) {
		return addElement(new Object(), type, initializer, x, y);
	}
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * Same as calling {@link #addElement(Object, GuiElements, Consumer, int, int)}
	 * with 0;0 as the X;Y positions.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> T addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer) {
		return addElement(id, type, initializer, 0, 0);
	}
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * Same as calling {@link #addElement(Object, GuiElements, Consumer, int, int)}
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
		return addElement(new Object(), type, initializer, 0, 0);
	}
	
	
	
	/**
	 * Deletes the element with the specified ID from this GUI.
	 * If no element is found with that ID, then this method does nothing.
	 *
	 * @param id the identifier of the element
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T removeElement(@NotNull Object id);
	
	/**
	 * Deletes the specified element from this GUI.
	 * If the element is not part of this GUI, then this method does nothing.
	 *
	 * @param element the element to remove
	 * @return the current instance (for chaining)
	 */
	T removeElement(@NotNull GuiElement<?> element);
	
	
	
	/**
	 * Runs the specified callback, executing all updates atomically.
	 * This means that the GUI won't be updated for the players
	 * until the callback has returned.
	 * This way the players can't observe intermediate states,
	 * only start and result states.
	 *
	 * @param updater the callback that updates this GUI
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T atomicUpdate(@NotNull Consumer<T> updater);
	
	
	
	/**
	 * Opens this GUI for the specified player.
	 * This method will do nothing if this GUI is already opened for the player.
	 * If the player has a GUI opened that it is not this one, then that GUI will be closed.
	 * This operation is only valid for modded players.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T openFor(@NotNull Player player);
	
	/**
	 * Closes this GUI for the specified player.
	 * This method will do nothing if this GUI is not opened for the player.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T closeFor(@NotNull Player player);
}
