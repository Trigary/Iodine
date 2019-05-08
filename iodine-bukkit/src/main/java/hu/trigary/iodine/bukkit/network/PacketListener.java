package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.network.handler.LoginPacketHandler;
import hu.trigary.iodine.bukkit.network.handler.PacketHandler;
import hu.trigary.iodine.common.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener implements PluginMessageListener {
	private final Logger logger = IodinePlugin.getInstance().getLogger();
	private final PacketType[] types = PacketType.values();
	private final PacketHandler[] handlers = new PacketHandler[types.length];
	
	public PacketListener() {
		handlers[PacketType.LOGIN.ordinal()] = new LoginPacketHandler();
	}
	
	
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (message.length == 0) {
			logger.warning("Received empty message from " + player.getName());
			//TODO option to disable warnings
			return;
		}
		
		PacketType type = PacketType.fromOrdinal(message[0]);
		if (type == null) {
			logger.warning("Received message with invalid type from " + player.getName());
			return;
		}
		
		PacketHandler handler = handlers[type.ordinal()];
		if (handler == null) {
			logger.warning("Received message with invalid type (" + type + ") from " + player.getName());
			return;
		}
		
		logger.info("Received message from " + player.getName()
				+ " of type " + type + ", length: " + message.length);
		
		try {
			handler.handle(player, message);
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Error while attempting to handle message", e);
		}
	}
}
