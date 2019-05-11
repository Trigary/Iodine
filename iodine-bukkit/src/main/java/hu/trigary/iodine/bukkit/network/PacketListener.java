package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.api.player.PlayerState;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.network.handler.GuiChangePacketHandler;
import hu.trigary.iodine.bukkit.network.handler.GuiClosePacketHandler;
import hu.trigary.iodine.bukkit.network.handler.LoginPacketHandler;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.network.handler.PacketHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * A class which listens to incoming messages and handles them,
 * delegating valid messages to the internally registered {@link PacketHandler} instances.
 */
public class PacketListener implements PluginMessageListener {
	private final IodinePlugin plugin;
	private final PacketType[] types = PacketType.values();
	private final PacketHandler[] handlers = new PacketHandler[types.length];
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link NetworkManager}.
	 *
	 * @param plugin the plugin instance
	 */
	public PacketListener(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		handlers[PacketType.CLIENT_LOGIN.getId()] = new LoginPacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CLOSE.getId()] = new GuiClosePacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CHANGE.getId()] = new GuiChangePacketHandler(plugin);
	}
	
	
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		PlayerState state = plugin.getPlayer(player).getState();
		if (state == PlayerState.INVALID) {
			plugin.logDebug("Ignoring message from invalid player {0}", player.getName());
			return;
		}
		
		if (message.length == 0) {
			plugin.logDebug("Received empty message from {0}, ignoring player", player.getName());
			plugin.getPlayer(player).setState(PlayerState.INVALID);
			return;
		}
		
		PacketType type = PacketType.fromId(message[0]);
		if (type == null) {
			plugin.logDebug("Received message with invalid type-id from {0}, ignoring player", player.getName());
			plugin.getPlayer(player).setState(PlayerState.INVALID);
			return;
		}
		
		PacketHandler handler = handlers[type.getId()];
		if (handler == null || handler.getTargetState() != state) {
			plugin.logDebug("Received message with invalid type ({0}) from {1}, ignoring player", type, player.getName());
			plugin.getPlayer(player).setState(PlayerState.INVALID);
			return;
		}
		
		try {
			plugin.logDebug("Received message with from {0} of type {1}, handling it", player.getName(), type);
			handler.handle(player, ByteBuffer.wrap(message, 1, message.length - 1));
		} catch (RuntimeException e) {
			plugin.logDebug("Error while handling message, ignoring player", e);
			plugin.getPlayer(player).setState(PlayerState.INVALID);
		}
	}
}
