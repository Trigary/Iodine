package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
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
	public void handle(@NotNull IodinePlayerImpl player, @NotNull ByteBuffer message) {
		GuiBaseImpl<?> gui = plugin.getGui().getGui(message.getInt());
		if (gui == null) {
			plugin.log(Level.OFF, "Network > closing GUI failed: no open GUI by id");
		} else {
			plugin.log(Level.OFF, "Network > closing GUI {0}", gui.getId());
			gui.closeForNoPacket(player, true);
		}
	}
}
