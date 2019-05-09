package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.api.PlayerState;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.network.handler.LoginPacketHandler;
import hu.trigary.iodine.bukkit.network.handler.PacketHandler;
import hu.trigary.iodine.common.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class PacketListener implements PluginMessageListener {
	private final IodinePlugin plugin;
	private final PacketType[] types = PacketType.values();
	private final PacketHandler[] handlers = new PacketHandler[types.length];
	
	public PacketListener(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		handlers[PacketType.LOGIN.ordinal()] = new LoginPacketHandler(plugin);
	}
	
	
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		PlayerState state = plugin.getPlayer().getState(player);
		if (state == PlayerState.INVALID) {
			plugin.logDebug("Ignoring message from invalid player: {0}", player.getName());
			return;
		}
		
		if (message.length == 0) {
			plugin.logDebug("Received empty message from {0}, ignoring player", player.getName());
			plugin.getPlayer().setState(player, PlayerState.INVALID);
			return;
		}
		
		PacketType type = PacketType.fromOrdinal(message[0]);
		if (type == null) {
			plugin.logDebug("Received message with invalid type-id from {0}, ignoring player", player.getName());
			plugin.getPlayer().setState(player, PlayerState.INVALID);
			return;
		}
		
		PacketHandler handler = handlers[type.ordinal()];
		if (handler == null || handler.getTargetState() != state) {
			plugin.logDebug("Received message with invalid type ({0}) from {1}, ignoring player", type, player.getName());
			plugin.getPlayer().setState(player, PlayerState.INVALID);
			return;
		}
		
		plugin.logDebug("Received message with from {0} of type {1}, handling it", player.getName(), type);
		try {
			handler.handle(player, message);
		} catch (Throwable t) {
			plugin.logDebug("Error while handling message, ignoring player", t);
			plugin.getPlayer().setState(player, PlayerState.INVALID);
		}
	}
}
