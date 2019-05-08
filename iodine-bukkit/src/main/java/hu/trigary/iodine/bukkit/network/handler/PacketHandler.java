package hu.trigary.iodine.bukkit.network.handler;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PacketHandler {
	void handle(Player player, byte[] message);
}
