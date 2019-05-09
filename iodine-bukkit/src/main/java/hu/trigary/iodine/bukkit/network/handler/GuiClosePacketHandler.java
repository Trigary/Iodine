package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class GuiClosePacketHandler extends PacketHandler {
	
	public GuiClosePacketHandler(@NotNull IodinePlugin plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void handle(@NotNull Player player, @NotNull ByteBuffer message) {
		IodineGuiImpl gui = plugin.getGui().getGui(message.getInt());
		if (gui != null) {
			gui.closedBy(player);
		}
	}
}
