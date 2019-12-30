package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class GuiManager {
	private final IodineMod mod;
	private IodineGui openGui;
	
	protected GuiManager(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	protected final IodineMod getMod() {
		return mod;
	}
	
	
	
	public final void packetOpenGui(@NotNull ByteBuffer buffer) {
		IodineGui gui = new IodineGui(mod, buffer.getInt());
		if (openGui != null) {
			closeOpenGui(false);
			mod.getLogger().debug("GuiManager > closing {} to open {}", openGui.getId(), gui.getId());
		}
		
		mod.getLogger().debug("GuiManager > opening {}", gui.getId());
		openGui = gui;
		gui.deserialize(buffer);
		openGuiImpl(gui);
	}
	
	public final void packetUpdateGui(@NotNull ByteBuffer buffer) {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > updating {}", openGui.getId());
			openGui.deserialize(buffer);
		} else {
			mod.getLogger().debug("GuiManager > update failed: no open GUIs");
		}
	}
	
	public final void packetCloseGui() {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > packet closing {}", openGui.getId());
			closeOpenGui(false);
		} else {
			mod.getLogger().info("Packet: Can't close GUI: it's no longer opened");
		}
	}
	
	public final void playerCloseGui() {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > player closing {}", openGui.getId());
			mod.getNetwork().send(PacketType.CLIENT_GUI_CLOSE, 4, b -> b.putInt(openGui.getId()));
			closeOpenGui(true);
		}
	}
	
	
	
	private void closeOpenGui(boolean byPlayer) {
		IodineGui gui = openGui;
		openGui = null;
		closeGuiImpl(gui, byPlayer);
	}
	
	protected abstract void openGuiImpl(@NotNull IodineGui gui);
	
	protected abstract void closeGuiImpl(@NotNull IodineGui gui, boolean byPlayer);
}
