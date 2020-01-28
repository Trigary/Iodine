package hu.trigary.iodine.client;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.gui.ElementManager;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.OverlayManager;
import hu.trigary.iodine.client.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The base class for the mod.
 */
public abstract class IodineMod {
	private Logger logger;
	private String version;
	private NetworkManager networkManager;
	private ElementManager elementManager;
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	
	
	/**
	 * Should be called when the mod gets activated, just before
	 * {@link #setup(NetworkManager, ElementManager, GuiManager, OverlayManager)}.
	 *
	 * @param logger the logger to use
	 * @param version the mod's version
	 */
	public final void initialize(@NotNull Logger logger, @NotNull String version) {
		this.logger = logger;
		this.version = version;
	}
	
	/**
	 * Should be called when the mod gets activated,
	 * just after {@link #initialize(Logger, String)}.
	 *
	 * @param networkManager the network manager instance
	 * @param elementManager the element manager instance
	 * @param guiManager the gui manager instance
	 * @param overlayManager the overlay manager instance
	 */
	public final void setup(@NotNull NetworkManager networkManager, @NotNull ElementManager elementManager,
			@NotNull GuiManager guiManager, @NotNull OverlayManager overlayManager) {
		this.networkManager = networkManager;
		this.elementManager = elementManager;
		this.guiManager = guiManager;
		this.overlayManager = overlayManager;
	}
	
	
	
	/**
	 * Gets the logger associated with this mod.
	 *
	 * @return this mod's logger.
	 */
	@NotNull
	@Contract(pure = true)
	public final Logger getLogger() {
		return logger;
	}
	
	/**
	 * Gets this mod's version.
	 *
	 * @return this mod's version
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
	 * Gets the element manager instance.
	 *
	 * @return the element manager
	 */
	@NotNull
	@Contract(pure = true)
	public final ElementManager getElementManager() {
		return elementManager;
	}
	
	/**
	 * Gets the gui manager instance.
	 *
	 * @return the gui manager
	 */
	@NotNull
	@Contract(pure = true)
	public final GuiManager getGuiManager() {
		return guiManager;
	}
	
	/**
	 * Gets the overlay manager instance.
	 *
	 * @return the overlay manager
	 */
	@NotNull
	@Contract(pure = true)
	public final OverlayManager getOverlayManager() {
		return overlayManager;
	}
	
	
	
	/**
	 * Should be called when the client joined a server and is ready to send packets.
	 */
	public final void onJoinedServer() {
		logger.info("Joined server, attempting login");
		networkManager.initialize();
		networkManager.send(PacketType.CLIENT_LOGIN, b -> b.putString(version));
	}
	
	/**
	 * Should be called when the client quits a server.
	 */
	public final void onQuitServer() {
		guiManager.playerCloseGui();
		overlayManager.closeOverlays();
	}
	
	/**
	 * Gets the client screen's width.
	 *
	 * @return the screen width
	 */
	@Contract(pure = true)
	public abstract int getScreenWidth();
	
	/**
	 * Gets the client screen's height.
	 *
	 * @return the screen height
	 */
	@Contract(pure = true)
	public abstract int getScreenHeight();
}
