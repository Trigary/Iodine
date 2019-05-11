package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.player.PlayerState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;

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
	 * Sets the action that should be executed when this GUI is opened for a player.
	 * GUIs are not allowed to be opened or closed in this callback.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	public abstract IodineGui setOpenAction(@Nullable BiConsumer<IodineGui, Player> action);
	
	/**
	 * Sets the action that should be executed when this GUI is closed for or closed by a player.
	 * GUIs are not allowed to be opened or closed in this callback.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	public abstract IodineGui setCloseAction(@Nullable BiConsumer<IodineGui, Player> action);
	
	
	
	/**
	 * Opens this GUI for the specified player.
	 * This method will do nothing if this GUI is already opened for the player.
	 * If the player has a GUI opened that it is not this one, then that GUI will be closed.
	 * This operation is only valid for modded players.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	public abstract IodineGui openFor(@NotNull Player player);
	
	/**
	 * Closes this GUI for the specified player.
	 * This method will do nothing if this GUI is not opened for the player.
	 *
	 * @param player the target player
	 * @return the current instance (for chaining)
	 */
	public abstract IodineGui closeFor(@NotNull Player player);
}
