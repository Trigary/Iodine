package hu.trigary.iodine.api;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The API plugins should use to have access to Iodine's features.
 * Instances of this class can be obtained through the {@link ServicesManager}.
 * The {@link #getInstance()} method does this internally.
 */
public interface IodineApi {
	/**
	 * Gets an instance of this class, using the {@link ServicesManager}.
	 *
	 * @return the API instance
	 */
	@NotNull
	@Contract(pure = true)
	static IodineApi getInstance() {
		return Bukkit.getServicesManager().getRegistration(IodineApi.class).getProvider();
	}
	
	
	
	/**
	 * Gets the data associated with the specified player.
	 *
	 * @param player the player to get data about
	 * @return the data associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	IodinePlayer getPlayer(@NotNull Player player);
	
	/**
	 * Gets whether the player is using the Iodine mod.
	 * For more information regarding edge cases see {@link IodinePlayer.State}.
	 *
	 * @param player the player to check
	 * @return whether the player's state is {@link IodinePlayer.State#MODDED}
	 */
	@Contract(pure = true)
	default boolean isModded(@NotNull Player player) {
		return getPlayer(player).getState() == IodinePlayer.State.MODDED;
	}
	
	
	
	/**
	 * Creates a new, empty GUI.
	 *
	 * @return a new GUI
	 */
	@NotNull
	@Contract(pure = true)
	IodineGui createGui();
	
	/**
	 * Creates a new, empty overlay.
	 *
	 * @param anchor the specified anchor
	 * @return a new overlay
	 */
	@NotNull
	@Contract(pure = true)
	IodineOverlay createOverlay(@NotNull IodineOverlay.Anchor anchor);
}
