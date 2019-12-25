package hu.trigary.iodine.client;

import hu.trigary.iodine.client.gui.GuiBaseManager;
import hu.trigary.iodine.client.network.NetworkManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public abstract class IodineModBase {
	private final Logger logger;
	private String version;
	private NetworkManager network;
	private GuiBaseManager gui;
	
	protected IodineModBase(@NotNull Logger logger) {
		this.logger = logger;
	}
	
	
	
	protected void initialize(@NotNull String version, @NotNull NetworkManager network, @NotNull GuiBaseManager gui) {
		this.version = version;
		this.network = network;
		this.gui = gui;
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
	public final GuiBaseManager getGui() {
		return gui;
	}
}
