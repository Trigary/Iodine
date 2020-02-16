package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

/**
 * The manager whose responsibility is managing the open {@link IodineGui}.
 */
public abstract class GuiManager {
	private final IodineMod mod;
	private IodineGui openGui;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
	protected GuiManager(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	/**
	 * Called when a packet was received instructing the client to open a gui.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public final void packetOpenGui(@NotNull InputBuffer buffer) {
		IodineGui gui = new IodineGui(mod, buffer.readInt());
		if (openGui != null) {
			closeOpenGui(false);
			mod.getLogger().debug("GuiManager > closing {} to open {}", openGui.getId(), gui.getId());
		}
		
		mod.getLogger().debug("GuiManager > opening {}", gui.getId());
		openGui = gui;
		gui.deserialize(buffer, false);
		openGuiImpl(gui);
	}
	
	/**
	 * Called when a packet was received instructing the client to update its open gui.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public final void packetUpdateGui(@NotNull InputBuffer buffer) {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > updating {}", openGui.getId());
			openGui.deserialize(buffer, true);
		} else {
			mod.getLogger().debug("GuiManager > update failed: no open GUIs");
		}
	}
	
	/**
	 * Called when a packet was received instructing the client to close its open gui.
	 */
	public final void packetCloseGui() {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > packet closing {}", openGui.getId());
			closeOpenGui(false);
		} else {
			mod.getLogger().info("Packet: Can't close GUI: it's no longer opened");
		}
	}
	
	/**
	 * Should be called when the client closes its open GUI.
	 * This method is internally called when the client quits the server.
	 */
	public final void playerCloseGui() {
		if (openGui != null) {
			mod.getLogger().debug("GuiManager > player closing {}", openGui.getId());
			mod.getNetworkManager().send(PacketType.CLIENT_GUI_CLOSE, b -> b.putInt(openGui.getId()));
			closeOpenGui(true);
		}
	}
	
	
	
	private void closeOpenGui(boolean byPlayer) {
		try (IodineGui gui = openGui) {
			openGui = null;
			closeGuiImpl(gui, byPlayer);
		}
	}
	
	/**
	 * Opens the specified gui screen.
	 *
	 * @param gui the gui to display
	 */
	protected abstract void openGuiImpl(@NotNull IodineGui gui);
	
	/**
	 * Closes the specified gui screen.
	 *
	 * @param gui the gui to close
	 * @param byPlayer whether this method is called thanks to a client-side event
	 */
	protected abstract void closeGuiImpl(@NotNull IodineGui gui, boolean byPlayer);
}
