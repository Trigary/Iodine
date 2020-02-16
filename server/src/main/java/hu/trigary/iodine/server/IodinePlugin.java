package hu.trigary.iodine.server;

import hu.trigary.iodine.api.IodineEvent;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.server.gui.IodineRootManager;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import hu.trigary.iodine.server.network.NetworkManager;
import hu.trigary.iodine.server.player.PlayerManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A base class for Iodine plugin main classes.
 */
public abstract class IodinePlugin {
	private Map<Class<? extends IodineEvent>, List<EventListener>> eventListeners;
	private Logger logger;
	private boolean debugLog;
	private String version;
	private NetworkManager networkManager;
	private PlayerManager playerManager;
	private IodineRootManager rootManager;
	private IodineApiImpl api;
	
	
	
	/**
	 * Should be called when the plugin gets activated,
	 * just before {@link #onEnabled(NetworkManager, PlayerManager, Stream)}.
	 *
	 * @param logger the logger to use
	 * @param debugLog whether debugging logging should be enabled
	 * @param version the plugin's version
	 */
	public final void initialize(@NotNull Logger logger, boolean debugLog, @NotNull String version) {
		eventListeners = new HashMap<>();
		this.logger = logger;
		this.debugLog = debugLog;
		this.version = version;
	}
	
	/**
	 * Should be called when the plugin gets activated,
	 * directly after {@link #initialize(Logger, boolean, String)}.
	 *
	 * @param networkManager the network manager implementation instance
	 * @param playerManager the player manager implementation instance
	 * @param onlinePlayers the currently online players
	 */
	public final void onEnabled(@NotNull NetworkManager networkManager,
			@NotNull PlayerManager playerManager, @NotNull Stream<UUID> onlinePlayers) {
		this.networkManager = networkManager;
		this.playerManager = playerManager;
		rootManager = new IodineRootManager(this);
		
		api = new IodineApiImpl(this);
		
		onlinePlayers
				.map(playerManager::getPlayer)
				.forEach(player -> networkManager.send(player, PacketType.SERVER_LOGIN_REQUEST));
	}
	
	/**
	 * Should be called when the plugin gets deactivated.
	 */
	public final void onDisabled() {
		log(Level.INFO, "Closing all open GUI instances");
		rootManager.closeAllRoots();
		api.clearInstance();
	}
	
	
	
	/**
	 * Gets this plugin's version.
	 *
	 * @return the plugin's version
	 */
	@NotNull
	@Contract(pure = true)
	public final String getVersion() {
		return version;
	}
	
	/**
	 * Gets the network manager instance.
	 *
	 * @return the network manager
	 */
	@NotNull
	@Contract(pure = true)
	public final NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	/**
	 * Gets the player wrapper associated with the specified UUID.
	 *
	 * @param player the requested player's {@link UUID}
	 * @return the wrapper instance associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	public final IodinePlayerBase getPlayer(@NotNull UUID player) {
		return playerManager.getPlayer(player);
	}
	
	/**
	 * Gets the GUI manager instance.
	 *
	 * @return the GUI manager
	 */
	@NotNull
	@Contract(pure = true)
	public final IodineRootManager getRootManager() {
		return rootManager;
	}
	
	
	
	/**
	 * Notifies the event listeners of the specified event.
	 *
	 * @param event the event to announce
	 * @param <T> the type of the event
	 */
	public final <T extends IodineEvent> void postEvent(@NotNull T event) {
		List<EventListener> listeners = eventListeners.get(event.getClass());
		if (listeners == null) {
			return;
		}
		
		for (EventListener listener : listeners) {
			try {
				//noinspection unchecked
				((Consumer<T>) listener.handler).accept(event);
			} catch (Throwable t) {
				log(Level.SEVERE, "Exception in " + listener.plugin + " plugin's event listener for "
						+ event.getClass().getSimpleName(), t);
			}
		}
	}
	
	/**
	 * Registers an event listener.
	 * Should only be called through the API.
	 *
	 * @param event the event to listen for
	 * @param handler the handler of the event
	 * @param plugin the exact name of the plugin that is registering the listener
	 * @param <T> the type of the event
	 */
	public final <T extends IodineEvent> void addEventListener(@NotNull Class<T> event,
			@NotNull String plugin, @NotNull Consumer<T> handler) {
		List<EventListener> listeners = eventListeners.computeIfAbsent(event, k -> new ArrayList<>());
		if (listeners.stream().anyMatch(listener -> listener.handler == handler)) {
			log(Level.WARNING, "Exact handler instance got registered again in plugin {0} for {1}",
					plugin, event.getSimpleName());
		}
		listeners.add(new EventListener(plugin, handler));
	}
	
	/**
	 * Unregisters an event listener.
	 * Should only be called through the API.
	 *
	 * @param event the event to listen for
	 * @param handler the handler of the event
	 * @param <T> the type of the event
	 * @return true if the handler was actually removed, false if it wasn't registered
	 */
	public final <T extends IodineEvent> boolean removeEventListener(@NotNull Class<T> event, @NotNull Consumer<T> handler) {
		List<EventListener> listeners = eventListeners.get(event);
		if (listeners == null) {
			return false;
		}
		
		if (listeners.size() == 1 && listeners.get(0).handler == handler) {
			eventListeners.remove(event);
			return true;
		} else {
			for (Iterator<EventListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
				if (iterator.next().handler == handler) {
					iterator.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Unregisters all event listeners linked to the specified plugin.
	 *
	 * @param plugin the exact name of the plugin whose listeners to remove
	 */
	public final void removePluginEventListeners(@NotNull String plugin) {
		eventListeners.values().removeIf(list -> list.removeIf(listener -> listener.plugin.equals(plugin)) && list.isEmpty());
	}
	
	
	
	/**
	 * Logs the specified message.
	 *
	 * @param level the log level to use ({@link Level#OFF} is converted to {@link Level#INFO} if debug logging is enabled)
	 * @param message the message to log
	 * @param params the message's parameters
	 */
	public final void log(@NotNull Level level, @NotNull String message, @NotNull Object... params) {
		level = level == Level.OFF && debugLog ? Level.INFO : level;
		logger.log(level, message, params);
	}
	
	/**
	 * Logs the specified message.
	 *
	 * @param level the log level to use ({@link Level#OFF} is converted to {@link Level#INFO} if debug logging is enabled)
	 * @param message the message to log
	 * @param cause the {@link Throwable} associated with the log message
	 */
	public final void log(@NotNull Level level, @NotNull String message, @NotNull Throwable cause) {
		level = level == Level.OFF && debugLog ? Level.INFO : level;
		logger.log(level, message, cause);
	}
	
	
	
	private static class EventListener {
		final String plugin;
		final Consumer<? extends IodineEvent> handler;
		
		EventListener(@NotNull String plugin, @NotNull Consumer<? extends IodineEvent> handler) {
			this.plugin = plugin;
			this.handler = handler;
		}
	}
}
