package hu.trigary.iodine.api.player;

/**
 * Represents the state of a player in terms of modded-GUI support.
 */
public enum PlayerState {
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
