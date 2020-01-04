package hu.trigary.iodine.api;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The API plugins should use to have access to Iodine's features.
 * An instance of this class can be obtained through the {@link #getInstance()} getter.
 * <br><br>
 * When the Iodine plugin gets disabled all {@link IodineGui} instances are closed
 * without calling the {@link IodineGui#onClosed(IodineGui.ClosedAction)} callback.
 * For this reason some plugins might have to listen to the plugin
 * disable event and gracefully close the GUIs there.
 * <br><br>
 * It is worth noting that generally speaking values which are representable on 15 bits are supported,
 * or 14 bits in case negative values are also valid. While the API works with 32 bit integers,
 * the implementation prefers 16 bit shorts and variable-length encoding to reduce payload size.
 */
public abstract class IodineApi {
	protected static IodineApi instance;
	
	/**
	 * Gets the API instance.
	 *
	 * @return an instance of this class
	 */
	@NotNull
	@Contract(pure = true)
	public static IodineApi getInstance() {
		return instance;
	}
	
	
	
	/**
	 * Gets the player wrapper associated with the specified UUID.
	 *
	 * @param player the requested player's {@link UUID}
	 * @return the wrapper instance associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	public abstract IodinePlayer getPlayer(@NotNull UUID player);
	
	/**
	 * Gets whether the player is using the Iodine mod.
	 * For more information regarding edge cases see {@link IodinePlayer.State}.
	 *
	 * @param player the player to check
	 * @return whether the player's state is {@link IodinePlayer.State#MODDED}
	 */
	@Contract(pure = true)
	public boolean isModded(@NotNull UUID player) {
		return getPlayer(player).isModded();
	}
	
	
	
	/**
	 * Creates a new, empty GUI.
	 *
	 * @return a new GUI
	 */
	@NotNull
	@Contract(pure = true)
	public abstract IodineGui createGui();
	
	/**
	 * Creates a new, empty overlay.
	 *
	 * @param anchor the specified anchor
	 * @param horizontalOffset the overlay's horizontal offset
	 * @param verticalOffset the overlay's vertical offset
	 * @return a new overlay
	 */
	@NotNull
	@Contract(pure = true)
	public abstract IodineOverlay createOverlay(@NotNull IodineOverlay.Anchor anchor, int horizontalOffset, int verticalOffset);
	
	
	
	/**
	 * Registers an event listener.
	 * Handler instances can be unregistered using the {@link #removeListener(Class, Consumer)} method.
	 *
	 * @param event the event to listen for
	 * @param handler the handler of the event
	 * @param <T> the type of the event
	 */
	public abstract <T extends IodineEvent> void addListener(@NotNull Class<T> event, @NotNull Consumer<T> handler);
	
	/**
	 * Unregisters an event listener.
	 * Handler instances can be registered using the {@link #addListener(Class, Consumer)} method.
	 *
	 * @param event the event to listen for
	 * @param handler the handler of the event
	 * @param <T> the type of the event
	 */
	public abstract <T extends IodineEvent> void removeListener(@NotNull Class<T> event, @NotNull Consumer<T> handler);
}
