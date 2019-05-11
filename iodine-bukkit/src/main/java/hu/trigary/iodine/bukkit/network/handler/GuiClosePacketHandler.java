package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.backend.PacketType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

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
	public void handle(@NotNull Player player, @NotNull ByteBuffer message) {
		IodineGuiImpl gui = plugin.getGui().get(message.getInt());
		if (gui != null) {
			gui.closeForNoPacket(plugin.getPlayer(player));
		}
	}
}
