package hu.trigary.iodine.client;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.gui.ElementManager;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.OverlayManager;
import hu.trigary.iodine.client.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class IodineMod {
	private Logger logger;
	private String version;
	private NetworkManager networkManager;
	private ElementManager elementManager;
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	
	
	public final void initialize(@NotNull Logger logger, @NotNull String version) {
		this.logger = logger;
		this.version = version;
	}
	
	public final void setup(@NotNull NetworkManager networkManager, @NotNull ElementManager elementManager,
			@NotNull GuiManager guiManager, @NotNull OverlayManager overlayManager) {
		this.networkManager = networkManager;
		this.elementManager = elementManager;
		this.guiManager = guiManager;
		this.overlayManager = overlayManager;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final Logger getLogger() {
		return logger;
	}
	
	@NotNull
	@Contract(pure = true)
	public final String getVersion() {
		return version;
	}
	
	@NotNull
	@Contract(pure = true)
	public final NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	@NotNull
	@Contract(pure = true)
	public final ElementManager getElementManager() {
		return elementManager;
	}
	
	@NotNull
	@Contract(pure = true)
	public final GuiManager getGuiManager() {
		return guiManager;
	}
	
	@NotNull
	@Contract(pure = true)
	public final OverlayManager getOverlayManager() {
		return overlayManager;
	}
	
	
	
	public final void onJoinedServer() {
		logger.info("Joined server, attempting login");
		networkManager.initialize();
		byte[] array = BufferUtils.serializeString(version);
		networkManager.send(PacketType.CLIENT_LOGIN, array.length + 2, buffer -> {
			buffer.putShort((short) array.length);
			buffer.put(array);
		});
	}
	
	public final void onQuitServer() {
		guiManager.playerCloseGui();
		overlayManager.closeOverlays();
	}
	
	@Contract(pure = true)
	public abstract int getScreenWidth();
	
	@Contract(pure = true)
	public abstract int getScreenHeight();
}
