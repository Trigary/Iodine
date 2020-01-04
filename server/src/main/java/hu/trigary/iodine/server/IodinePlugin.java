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
 * The Iodine plugin's main class. //TODO docs
 */
public abstract class IodinePlugin {
	private Map<Class<? extends IodineEvent>, List<Consumer<? extends IodineEvent>>> eventListeners;
	private boolean debugLog;
	private String version;
	private NetworkManager networkManager;
	private PlayerManager playerManager;
	private IodineRootManager rootManager;
	
	
	
	//TODO docs
	public final void initialize(boolean debugLog, @NotNull String version) {
		eventListeners = new HashMap<>();
		this.debugLog = debugLog;
		this.version = version;
	}
	
	//TODO docs
	public final void onEnabled(@NotNull NetworkManager networkManager,
			@NotNull PlayerManager playerManager, @NotNull Stream<UUID> onlinePlayers) {
		this.networkManager = networkManager;
		this.playerManager = playerManager;
		rootManager = new IodineRootManager(this);
		
		new IodineApiImpl(this).installInstance();
		
		onlinePlayers
				.map(playerManager::getPlayer)
				.forEach(player -> networkManager.send(player, PacketType.SERVER_LOGIN_REQUEST));
	}
	
	//TODO docs, must be able to send packets her
	public final void onDisabled() {
		log(Level.INFO, "Closing all open GUI instances");
		rootManager.closeAllGuiInstances();
	}
	
	
	
	//TODO docs
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
	public final IodineRootManager getGuiManager() {
		return rootManager;
	}
	
	
	
	//TODO docs
	public final <T extends IodineEvent> void postEvent(@NotNull T event) {
		List<Consumer<? extends IodineEvent>> consumers = eventListeners.get(event.getClass());
		if (consumers != null) {
			for (Consumer<? extends IodineEvent> consumer : consumers) {
				//noinspection unchecked
				((Consumer<T>) consumer).accept(event);
			}
		}
	}
	
	//TODO docs
	public final <T extends IodineEvent> void changeEventListener(@NotNull Class<T> event,
			@NotNull Consumer<T> handler, boolean add) {
		List<Consumer<? extends IodineEvent>> consumers = eventListeners.computeIfAbsent(event, k -> new ArrayList<>());
		if (add) {
			consumers.add(handler);
		} else {
			consumers.remove(handler);
		}
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
		getLogger().log(level, message, params);
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
		getLogger().log(level, message, cause);
	}
	
	//TODO docs
	@NotNull
	@Contract(pure = true)
	protected abstract Logger getLogger();
}
