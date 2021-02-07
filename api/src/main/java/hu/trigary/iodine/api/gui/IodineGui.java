package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a GUI (General User Interface) that can be opened
 * for players who are in the {@link IodinePlayer.State#MODDED} state.
 */
public interface IodineGui extends IodineRoot<IodineGui> {
	/**
	 * Sets the action that should be executed when this GUI is closed by a player or a plugin.
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
		 * @param byPlayer true if the player closed the GUI (eg. by pressing escape, disconnecting,
		 * entering {@link IodinePlayer.State#INVALID}), false is a plugin is responsible (eg. by closing this GUI, opening another one)
		 */
		void accept(@NotNull IodineGui gui, @NotNull IodinePlayer player, boolean byPlayer);
	}
}
