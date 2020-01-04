package hu.trigary.iodine.server.player;

import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.IodineOverlayImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The manager whose responsibility is linking Minecraft player
 * and {@link IodinePlayerBase} instances, without causing memory leaks.
 */
public abstract class PlayerManager {
	private final Map<UUID, IodinePlayerBase> players = new HashMap<>();
	private final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by the {@link IodinePlugin} implementation.
	 *
	 * @param plugin the plugin instance
	 */
	protected PlayerManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	/**
	 * Gets the data associated with the specified player.
	 *
	 * @param player the player to get data about
	 * @return the data associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	public final IodinePlayerBase getPlayer(@NotNull UUID player) {
		IodinePlayerBase iodinePlayer = players.get(player);
		if (iodinePlayer == null) {
			iodinePlayer = tryCreatePlayer(player);
			plugin.log(Level.OFF, "PlayerManager > created {0}", iodinePlayer.getName());
			players.put(player, iodinePlayer);
		}
		return iodinePlayer;
	}
	
	//TODO docs
	@NotNull
	protected abstract IodinePlayerBase tryCreatePlayer(@NotNull UUID player);
	
	
	
	//TODO docs
	protected final void onPlayerQuit(@NotNull UUID player) {
		//can't call remove yet: the gui close callback might still require the instance
		IodinePlayerBase iodinePlayer = players.get(player);
		if (iodinePlayer == null) {
			return;
		}
		
		plugin.log(Level.OFF, "PlayerManager > removing {0}", iodinePlayer.getName());
		if (iodinePlayer.getOpenGui() != null) {
			iodinePlayer.getOpenGui().closeForNoPacket(iodinePlayer, true);
		}
		for (IodineOverlay overlay : iodinePlayer.getOverlays()) {
			((IodineOverlayImpl) overlay).closeForNoPacket(iodinePlayer, true);
		}
		players.remove(player);
		plugin.log(Level.OFF, "PlayerManager > removed {0}", iodinePlayer.getName());
	}
}
