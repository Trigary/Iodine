package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.gui.element.GuiElement;
import hu.trigary.iodine.api.player.PlayerState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a GUI (General User Interface) that can be opened
 * for players who are in the {@link PlayerState#MODDED} state.
 */
public abstract class IodineGui {
	/**
	 * Gets the players who have this GUI opened.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the players who have this GUI opened
	 */
	@NotNull
	@Contract(pure = true)
	public abstract Collection<Player> getViewers();
	
	/**
	 * Gets the element which has the specified ID.
	 * Returns null if no matching elements were found.
	 *
	 * @param id the ID to search for
	 * @return the matching element or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	public abstract GuiElement getElement(@NotNull Object id);
	
	/**
	 * Gets the elements that have been added to this GUI.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the elements this GUI contains
	 */
	@Nullable
	@Contract(pure = true)
	public abstract Collection<GuiElement> getAllElements();
	
	
	
	/**
	 * Sets the action that should be executed when this GUI is opened for a player.
	 * GUIs are not allowed to be opened or closed in this callback.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public abstract IodineGui setOpenAction(@Nullable BiConsumer<IodineGui, Player> action);
	
	/**
	 * Sets the action that should be executed when this GUI is closed for or closed by a player.
	 * GUIs are not allowed to be opened or closed in this callback.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public abstract IodineGui setCloseAction(@Nullable BiConsumer<IodineGui, Player> action);
	
	
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * The initializer function atomically initializes the new element.
	 * The element will be accessible using the specified ID.
	 * This ID must be unique.
	 * If ID-based access is not required, then optionally
	 * {@link #addElement(Class, Consumer)} can be used instead.
	 *
	 * @param id id the identifier of the element
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <T> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public abstract <T extends GuiElement> IodineGui addElement(@NotNull Object id,
			@NotNull Class<T> type, @NotNull Consumer<T> initializer);
	
	/**
	 * Adds a new element of the specified type to this GUI.
	 * The initializer function atomically initializes the new element.
	 * The element will have an internally created ID.
	 * If ID-based access is required, then {@link #addElement(Object, Class, Consumer)}
	 * should be used instead.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <T> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public <T extends GuiElement> IodineGui addElement(@NotNull Class<T> type,
			@NotNull Consumer<T> initializer) {
		return addElement(new Object(), type, initializer);
	}
	
	/**
	 * Deletes the element with the specified ID from this GUI.
	 * If no element is found with that ID, then this method does nothing.
	 *
	 * @param id the identifier of the element
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public abstract IodineGui removeElement(@NotNull Object id);
	
	
	
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
	public abstract IodineGui atomicUpdate(@NotNull Consumer<IodineGui> updater);
	
	
	
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
	public abstract IodineGui openFor(@NotNull Player player);
	
	/**
	 * Closes this GUI for the specified player.
	 * This method will do nothing if this GUI is not opened for the player.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	@NotNull
	public abstract IodineGui closeFor(@NotNull Player player);
}
