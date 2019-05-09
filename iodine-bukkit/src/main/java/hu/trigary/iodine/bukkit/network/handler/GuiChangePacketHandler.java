package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.bukkit.IodinePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class GuiChangePacketHandler extends PacketHandler {
	
	public GuiChangePacketHandler(@NotNull IodinePlugin plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void handle(@NotNull Player player, @NotNull ByteBuffer message) {
		//TODO
	}
}
