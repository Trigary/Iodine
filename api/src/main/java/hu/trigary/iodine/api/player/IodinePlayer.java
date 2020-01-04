package hu.trigary.iodine.api.player;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * A class that associates Iodine data with a Minecraft player.
 * References to this class' instances should must disposed of as soon as the player logs off.
 */
public interface IodinePlayer {
	/**
	 * Gets the {@link UUID} player that this instance is associated with.
	 *
	 * @return the player's UUID
	 */
	@NotNull
	@Contract(pure = true)
	UUID getPlayer();
	
	/**
	 * Gets the actual player object that this instance is associated with.
	 *
	 * @param clazz the type of the player instance
	 * @param <T> the type of the player instance
	 * @return the real player instance
	 */
	@NotNull
	@Contract(pure = true)
	<T> T getPlayer(@NotNull Class<T> clazz);
	
	/**
	 * Gets the player's name.
	 *
	 * @return the player's name
	 */
	@NotNull
	@Contract(pure = true)
	String getName();
	
	
	
	/**
	 * Gets the state this player is currently in.
	 *
	 * @return the state of the player
	 */
	@NotNull
	@Contract(pure = true)
	State getState();
	
	//TODO docs
	@Contract(pure = true)
	default boolean isModded() {
		return getState() == State.MODDED;
	}
	
	
	
	//TODO docs
	void sendMessage(@NotNull String message);
	
	//TODO docs
	default void sendMessage(@NotNull String[] message) {
		for (String line : message) {
			sendMessage(line);
		}
	}
	
	
	
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
	 *
	 * @param overlay the overlay to close
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
