package hu.trigary.iodine.api.player;

import hu.trigary.iodine.api.gui.IodineGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class that associates Iodine data with a Bukkit player.
 * This class should be used like the {@link Player} class:
 * references to this class' instances must be disposed of as soon as the player logs off.
 * Otherwise memory leaks and unintended consequences follow.
 */
public abstract class IodinePlayer {
	/**
	 * Gets the Bukkit player that this instance is associated with.
	 *
	 * @return the Bukkit player instance
	 */
	@NotNull
	@Contract(pure = true)
	public abstract Player getPlayer();
	
	/**
	 * Gets the state this player is currently in.
	 *
	 * @return the state of the player
	 */
	@NotNull
	@Contract(pure = true)
	public abstract PlayerState getState();
	
	/**
	 * Gets the GUI that this player currently has opened.
	 * Returns null if this player doesn't have any opened.
	 * This operation is also valid for non-modded players,
	 * but will always return null.
	 *
	 * @return the GUI this player is viewing or null,
	 * if the player doesn't have one opened
	 */
	@Nullable
	@Contract(pure = true)
	public abstract IodineGui getOpenGui();
	
	
	
	/**
	 * Closes this player's currently opened {@link IodineGui}.
	 * Doesn't do anything if the player doesn't have one opened.
	 * This operation is only valid for modded players.
	 */
	public abstract void closeOpenGui();
}