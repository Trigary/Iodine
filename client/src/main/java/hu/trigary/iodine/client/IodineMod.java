package hu.trigary.iodine.client;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.gui.GuiElementManager;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.OverlayManager;
import hu.trigary.iodine.client.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class IodineMod {
	private Logger logger;
	private String version;
	private NetworkManager network;
	private GuiElementManager element;
	private GuiManager gui;
	private OverlayManager overlay;
	
	
	
	protected final void initializeFirst(@NotNull Logger logger, @NotNull String version) {
		this.logger = logger;
		this.version = version;
	}
	
	protected final void initializeSecond(@NotNull NetworkManager network, @NotNull GuiElementManager element,
			@NotNull GuiManager gui, @NotNull OverlayManager overlay) {
		this.network = network;
		this.element = element;
		this.gui = gui;
		this.overlay = overlay;
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
	public final NetworkManager getNetwork() {
		return network;
	}
	
	@NotNull
	@Contract(pure = true)
	public final GuiElementManager getElement() {
		return element;
	}
	
	@NotNull
	@Contract(pure = true)
	public final GuiManager getGui() {
		return gui;
	}
	
	@NotNull
	@Contract(pure = true)
	public final OverlayManager getOverlay() {
		return overlay;
	}
	
	
	
	public final void onJoinedServer() {
		logger.info("Joined server, attempting login");
		network.initialize();
		byte[] array = BufferUtils.serializeString(version);
		network.send(PacketType.CLIENT_LOGIN, array.length + 2, buffer -> {
			buffer.putShort((short) array.length);
			buffer.put(array);
		});
	}
	
	public final void onQuitServer() {
		gui.playerCloseGui();
		overlay.closeOverlays();
	}
	
	@Contract(pure = true)
	public abstract int getScreenWidth();
	
	@Contract(pure = true)
	public abstract int getScreenHeight();
}
