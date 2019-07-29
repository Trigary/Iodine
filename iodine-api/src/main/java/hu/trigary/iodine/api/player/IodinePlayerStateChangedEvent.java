package hu.trigary.iodine.api.player;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An event that gets called when the state of a player just changed.
 * In most cases, this is going to be a change
 * from {@link PlayerState#VANILLA} to {@link PlayerState#MODDED}.
 */
public final class IodinePlayerStateChangedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final IodinePlayer player;
	private final PlayerState oldState;
	private final PlayerState newState;
	
	/**
	 * Creates a new instance of this event.
	 * This should only be called by the {@link IodinePlayer} implementation.
	 *
	 * @param player the player whose state changed
	 * @param oldState the previous state of the player
	 * @param newState the current state of the player
	 */
	public IodinePlayerStateChangedEvent(@NotNull IodinePlayer player,
			@NotNull PlayerState oldState, @NotNull PlayerState newState) {
		this.player = player;
		this.oldState = oldState;
		this.newState = newState;
	}
	
	
	
	@Contract(pure = true)
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Contract(pure = true)
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	
	
	/**
	 * Gets the player whose state changed.
	 *
	 * @return the player who state changed
	 */
	@Contract(pure = true)
	public IodinePlayer getPlayer() {
		return player;
	}
	
	/**
	 * Gets the new old of the player.
	 *
	 * @return the previous state of the player
	 */
	@Contract(pure = true)
	public PlayerState getOldState() {
		return oldState;
	}
	
	/**
	 * Gets the new state of the player.
	 * Same as calling {@link IodinePlayer#getState()} under normal circumstances.
	 *
	 * @return the current state of the player
	 */
	@Contract(pure = true)
	public PlayerState getNewState() {
		return newState;
	}
	
	/**
	 * Utility methods, returns whether this event represents a successful Iodine login.
	 *
	 * @return true if {@link #getOldState()} is {@link PlayerState#VANILLA}
	 * and {@link #getNewState()} is {@link PlayerState#MODDED}
	 */
	@Contract(pure = true)
	public boolean isFromVanillaToModded() {
		return oldState == PlayerState.VANILLA && newState == PlayerState.MODDED;
	}
}
