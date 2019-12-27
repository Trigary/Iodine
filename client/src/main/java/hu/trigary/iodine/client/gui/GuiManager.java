package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class GuiManager {
	private final IodineMod mod;
	private IodineGui openGui;
	
	protected GuiManager(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	public final void packetOpenGui(@NotNull ByteBuffer buffer) {
		IodineGui gui = new IodineGui(mod, buffer.getInt());
		if (openGui != null) {
			closeOpenGui("Packet: Closed GUI to open another: ");
		}
		
		openGui = gui;
		gui.deserialize(buffer);
		openGuiImpl(gui);
		mod.getLogger().info("Packet: Opened GUI: " + gui.getId());
	}
	
	public final void packetUpdateGui(@NotNull ByteBuffer buffer) {
		if (openGui != null) {
			openGui.deserialize(buffer);
			mod.getLogger().info("Packet: Updated GUI: " + openGui.getId());
		} else {
			mod.getLogger().info("Packet: Can't update GUI: it's no longer opened");
		}
	}
	
	public final void packetCloseGui() {
		if (openGui != null) {
			closeOpenGui("Packet: Closed GUI: ");
		} else {
			mod.getLogger().info("Packet: Can't close GUI: it's no longer opened");
		}
	}
	
	public final void playerCloseGui() {
		mod.getNetwork().send(PacketType.CLIENT_GUI_CLOSE, 4, b -> b.putInt(openGui.getId()));
		closeOpenGui("Player closed GUI: ");
	}
	
	
	
	private void closeOpenGui(@NotNull String message) {
		closeGuiImpl(openGui);
		mod.getLogger().info(message + openGui.getId());
		openGui = null;
	}
	
	protected abstract void openGuiImpl(@NotNull IodineGui gui);
	
	protected abstract void closeGuiImpl(@NotNull IodineGui gui);
}
