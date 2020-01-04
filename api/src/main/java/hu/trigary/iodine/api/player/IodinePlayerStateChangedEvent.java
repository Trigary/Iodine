package hu.trigary.iodine.api.player;

import hu.trigary.iodine.api.IodineEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An event that gets called when the state of a player just changed.
 * In most cases, this is going to be a change
 * from {@link IodinePlayer.State#VANILLA} to {@link IodinePlayer.State#MODDED}.
 */
public final class IodinePlayerStateChangedEvent implements IodineEvent {
	private final IodinePlayer player;
	private final IodinePlayer.State oldState;
	private final IodinePlayer.State newState;
	
	/**
	 * Creates a new instance of this event.
	 * This should only be called by the {@link IodinePlayer} implementation.
	 *
	 * @param player the player whose state changed
	 * @param oldState the previous state of the player
	 * @param newState the current state of the player
	 */
	public IodinePlayerStateChangedEvent(@NotNull IodinePlayer player,
			@NotNull IodinePlayer.State oldState, @NotNull IodinePlayer.State newState) {
		this.player = player;
		this.oldState = oldState;
		this.newState = newState;
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
	public IodinePlayer.State getOldState() {
		return oldState;
	}
	
	/**
	 * Gets the new state of the player.
	 * Same as calling {@link IodinePlayer#getState()} under normal circumstances.
	 *
	 * @return the current state of the player
	 */
	@Contract(pure = true)
	public IodinePlayer.State getNewState() {
		return newState;
	}
}
