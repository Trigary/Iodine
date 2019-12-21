package hu.trigary.iodine.api.player;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A class that associates Iodine data with a Bukkit player.
 * This class should be used like the {@link Player} class:
 * references to this class' instances must be disposed of as soon as the player logs off.
 * Otherwise memory leaks and unintended consequences follow.
 */
public interface IodinePlayer {
	/**
	 * Gets the Bukkit player that this instance is associated with.
	 *
	 * @return the Bukkit player instance
	 */
	@NotNull
	@Contract(pure = true)
	Player getPlayer();
	
	/**
	 * Gets the state this player is currently in.
	 *
	 * @return the state of the player
	 */
	@NotNull
	@Contract(pure = true)
	State getState();
	
	
	
	/**
	 * Gets the GUI that this player currently has opened.
	 * Returns null if this player doesn't have any opened.
	 * This operation is also valid for non-modded players,
	 * and will always return null.
	 *
	 * @return the GUI this player is viewing or null,
	 * if the player doesn't have one opened
	 */
	@Nullable
	@Contract(pure = true)
	IodineGui getOpenGui();
	
	/**
	 * Closes this player's currently opened {@link IodineGui}.
	 * Doesn't do anything if the player doesn't have one opened.
	 * This operation is also valid for non-modded players,
	 * and will always do nothing.
	 */
	void closeOpenGui();
	
	
	
	/**
	 * Gets the overlays that this player has displayed.
	 * This operation is also valid for non-modded players,
	 * and will always return an empty collection.
	 *
	 * @return the overlays this player has displayed
	 */
	@NotNull
	@Contract(pure = true)
	Set<IodineOverlay> getOverlays();
	
	/**
	 * Closes this player's specified {@link IodineOverlay}.
	 * Doesn't do anything if the player doesn't have it displayed.
	 * This operation is also valid for non-modded players,
	 * and will always do nothing.
	 */
	void closeOverlay(@NotNull IodineOverlay overlay);
	
	
	
	/**
	 * Represents the state of a player in terms of modded-GUI support.
	 */
	enum State {
		/**
		 * The player is yet to log in, assuming that the player is not using the mod.
		 * Even modded players start out with this state when they join.
		 * <br><br>
		 * Players of this state might be become {@link #MODDED} or {@link #INVALID} later on.
		 */
		VANILLA,
		
		/**
		 * The player successfully logged in with a valid mod version.
		 * <br><br>
		 * Players of this state might be become {@link #INVALID} later on.
		 */
		MODDED,
		
		/**
		 * The player is using the mod, but there are communication issues,
		 * probably due to a mod version mismatch.
		 * <br><br>
		 * Players of this state are guaranteed to stay in this state until they log out.
		 */
		INVALID
	}
}
