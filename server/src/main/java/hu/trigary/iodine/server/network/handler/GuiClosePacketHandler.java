package hu.trigary.iodine.server.network.handler;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.network.PacketListener;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * The handler of {@link PacketType#CLIENT_GUI_CLOSE}.
 */
public class GuiClosePacketHandler extends PacketHandler {
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link PacketListener}.
	 *
	 * @param plugin the plugin instance
	 */
	public GuiClosePacketHandler(@NotNull IodinePlugin plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void handle(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {
		IodineRootImpl<?> gui = getPlugin().getRootManager().getRoot(buffer.readInt());
		if (gui == null) {
			getPlugin().log(Level.OFF, "Network > closing GUI failed: no open GUI by id");
		} else {
			getPlugin().log(Level.OFF, "Network > closing GUI {0}", gui.getId());
			gui.closeForNoPacket(player, true);
		}
	}
}
