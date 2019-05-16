package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.gui.container.base.GuiParent;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.player.PlayerState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Represents a GUI (General User Interface) that can be opened
 * for players who are in the {@link PlayerState#MODDED} state.
 */
public interface IodineGui extends GuiParent<IodineGui> {
	/**
	 * Gets the players who have this GUI opened.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the players who have this GUI opened
	 */
	@NotNull
	@Contract(pure = true)
	Collection<Player> getViewers();
	
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
	@NotNull <E extends GuiElement<E>> E addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer, int x, int y);
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * The parent of the element will be this GUI.
	 * The initializer function atomically initializes the new element.
	 * The element will have an internally created ID.
	 * If ID-based access is required, then {@link #addElement(Object, GuiElements, Consumer, int, int)}
	 * should be used instead.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param x the X component of the element's render position
	 * @param y the Y component of the element's render position
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> E addElement(@NotNull GuiElements<E> type,
			@NotNull Consumer<E> initializer, int x, int y) {
		return addElement(new Object(), type, initializer, x, y);
	}
	
	/**
	 * Deletes the element with the specified ID from this GUI.
	 * If no element is found with that ID, then this method does nothing.
	 *
	 * @param id the identifier of the element
	 * @return the current instance (for chaining)
	 */
	@NotNull
	IodineGui removeElement(@NotNull Object id);
	
	
	
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
	IodineGui atomicUpdate(@NotNull Consumer<IodineGui> updater);
	
	
	
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
	IodineGui openFor(@NotNull Player player);
	
	/**
	 * Closes this GUI for the specified player.
	 * This method will do nothing if this GUI is not opened for the player.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	IodineGui closeFor(@NotNull Player player);
	
	/**
	 * Sets the action that should be executed when this GUI is closed by a player.
	 * GUIs are not allowed to be opened or closed in this callback.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	IodineGui onClosed(@Nullable ClosedAction action);
	
	
	
	/**
	 * The handler of the closed action.
	 */
	@FunctionalInterface
	interface ClosedAction {
		/**
		 * Handles the closed action.
		 *
		 * @param gui the GUI that was closed
		 * @param player the player that closed the GUI
		 */
		void accept(@NotNull IodineGui gui, @NotNull Player player);
	}
}
