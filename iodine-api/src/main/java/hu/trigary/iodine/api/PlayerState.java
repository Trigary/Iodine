package hu.trigary.iodine.api;

/**
 * Represents the state of a player in terms of modded-UI support.
 */
public enum PlayerState {
	/**
	 * The player is yet to log in, assuming that the player is not using the mod.
	 * Even modded players start out with this state when they join.
	 */
	VANILLA,
	
	/**
	 * The player successfully logged in with a valid mod version.
	 */
	MODDED,
	
	/**
	 * The player is using the mod, but there are communication issues,
	 * probably due to a mod version mismatch.
	 */
	INVALID
}
