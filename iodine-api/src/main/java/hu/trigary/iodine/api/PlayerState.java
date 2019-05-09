package hu.trigary.iodine.api;

/**
 * Represents the state of a player in terms of modded-UI support.
 */
public enum PlayerState {
	/**
	 * The player is yet to log in, but not enough time has
	 * passed to assume that the player is not using the mod.
	 */
	UNKNOWN,
	
	/**
	 * The player has failed to login for some time now,
	 * assuming that the player is not using the mod.
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
