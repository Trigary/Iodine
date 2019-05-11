package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.bukkit.api.IodineApiImpl;
import hu.trigary.iodine.bukkit.api.player.IodinePlayerImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * The Iodine plugin's main class.
 */
public class IodinePlugin extends JavaPlugin implements Listener {
	private static final boolean DEBUG_LOG = true;
	private NetworkManager networkManager;
	private PlayerManager playerManager;
	private GuiManager guiManager;
	
	@Override
	public void onEnable() {
		IodineApiImpl api = new IodineApiImpl(this);
		Bukkit.getServicesManager().register(IodineApi.class, api, this, ServicePriority.Normal);
		
		Bukkit.getPluginManager().registerEvents(this, this);
		networkManager = new NetworkManager(this);
		playerManager = new PlayerManager(this);
		guiManager = new GuiManager(this);
		
		Bukkit.getPluginManager().registerEvents(new TestCommandListener(), this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDisable(PluginDisableEvent event) {
		//this fires before the plugin is disabled, therefore eg. packets can still be sent
		if (event.getPlugin() == this) {
			guiManager.closeAll();
		}
	}
	
	
	
	/**
	 * Gets the network manager instance.
	 *
	 * @return the network manager
	 */
	@NotNull
	@Contract(pure = true)
	public NetworkManager getNetwork() {
		return networkManager;
	}
	
	/**
	 * Gets the data associated with the specified player.
	 *
	 * @param player the player to get data about
	 * @return the data associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	public IodinePlayerImpl getPlayer(@NotNull Player player) {
		return playerManager.getPlayer(player);
	}
	
	/**
	 * Gets the GUI manager instance.
	 *
	 * @return the GUI manager
	 */
	@NotNull
	@Contract(pure = true)
	public GuiManager getGui() {
		return guiManager;
	}
	
	
	
	/**
	 * Logs the specified message if debug logging is enabled.
	 *
	 * @param message the message to log
	 * @param params the message's parameters
	 */
	public void logDebug(String message, Object... params) {
		if (DEBUG_LOG) {
			getLogger().log(Level.INFO, message, params);
		}
	}
	
	/**
	 * Logs the specified message if debug logging is enabled.
	 *
	 * @param message the message to log
	 * @param cause the {@link Throwable} associated with the log message
	 */
	public void logDebug(String message, Throwable cause) {
		if (DEBUG_LOG) {
			getLogger().log(Level.INFO, message, cause);
		}
	}
}
