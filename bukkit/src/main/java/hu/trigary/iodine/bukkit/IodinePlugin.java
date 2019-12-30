package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.gui.GuiBaseManager;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import hu.trigary.iodine.bukkit.player.PlayerManager;
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
	private boolean debugLog;
	private NetworkManager networkManager;
	private PlayerManager playerManager;
	private GuiBaseManager guiBaseManager;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		debugLog = getConfig().getBoolean("debug-log");
		
		Bukkit.getServicesManager().register(IodineApi.class,
				new IodineApiImpl(this), this, ServicePriority.Normal);
		
		Bukkit.getPluginManager().registerEvents(this, this);
		networkManager = new NetworkManager(this);
		playerManager = new PlayerManager(this);
		guiBaseManager = new GuiBaseManager(this);
		
		Bukkit.getPluginManager().registerEvents(new TestCommandListener(this), this);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			networkManager.send(player, PacketType.SERVER_LOGIN_REQUEST);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onDisable(PluginDisableEvent event) {
		log(Level.INFO, "Closing all open GUI instances");
		//this fires before the plugin is disabled, therefore eg. packets can still be sent
		if (event.getPlugin() == this) {
			guiBaseManager.closeAllGuiInstances();
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
	public GuiBaseManager getGui() {
		return guiBaseManager;
	}
	
	
	
	/**
	 * Logs the specified message.
	 *
	 * @param level the log level to use ({@link Level#OFF} is converted to {@link Level#INFO} if debug logging is enabled)
	 * @param message the message to log
	 * @param params the message's parameters
	 */
	public void log(@NotNull Level level, @NotNull String message, @NotNull Object... params) {
		level = level == Level.OFF && debugLog ? Level.INFO : level;
		getLogger().log(level, message, params);
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param level the log level to use ({@link Level#OFF} is converted to {@link Level#INFO} if debug logging is enabled)
	 * @param message the message to log
	 * @param cause the {@link Throwable} associated with the log message
	 */
	public void log(@NotNull Level level, @NotNull String message, @NotNull Throwable cause) {
		level = level == Level.OFF && debugLog ? Level.INFO : level;
		getLogger().log(level, message, cause);
	}
}
