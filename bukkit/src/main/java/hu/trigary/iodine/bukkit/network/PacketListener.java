package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.backend.ChatUtils;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.network.handler.GuiChangePacketHandler;
import hu.trigary.iodine.bukkit.network.handler.GuiClosePacketHandler;
import hu.trigary.iodine.bukkit.network.handler.LoginPacketHandler;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.network.handler.PacketHandler;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.logging.Level;

/**
 * A class which listens to incoming messages and handles them,
 * delegating valid messages to the internally registered {@link PacketHandler} instances.
 */
public class PacketListener implements PluginMessageListener {
	private final IodinePlugin plugin;
	private final PacketHandler[] handlers = new PacketHandler[PacketType.getHighestId() + 1];
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link NetworkManager}.
	 *
	 * @param plugin the plugin instance
	 */
	public PacketListener(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		handlers[PacketType.CLIENT_LOGIN.getUnsignedId()] = new LoginPacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CLOSE.getUnsignedId()] = new GuiClosePacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CHANGE.getUnsignedId()] = new GuiChangePacketHandler(plugin);
	}
	
	
	
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		IodinePlayer.State state = iodinePlayer.getState();
		if (state == IodinePlayer.State.INVALID) {
			plugin.log(Level.OFF, "Ignoring message from invalid player {0}", player.getName());
			return;
		}
		
		if (message.length == 0) {
			plugin.log(Level.INFO, "Received empty message from {0}, ignoring player", player.getName());
			ignore(iodinePlayer);
			return;
		}
		
		PacketType type = PacketType.fromId(message[0]);
		if (type == null) {
			plugin.log(Level.INFO, "Received message with invalid type-id ({0}) from {1}, ignoring player",
					message[0], player.getName());
			ignore(iodinePlayer);
			return;
		}
		
		PacketHandler handler = handlers[type.getUnsignedId()];
		if (handler == null) {
			plugin.log(Level.INFO, "Received message with invalid type {0} from {1}, ignoring player", type, player.getName());
			ignore(iodinePlayer);
			return;
		}
		
		if (handler.getTargetState() != state) {
			plugin.log(Level.INFO, "Received message of type {0} from {1} in (invalid) state: {2}",
					type, player.getName(), state);
			return;
		}
		
		try {
			plugin.log(Level.OFF, "Received message with from {0} of type {1}, handling it", player.getName(), type);
			handler.handle(iodinePlayer, ByteBuffer.wrap(message, 1, message.length - 1));
		} catch (Throwable t) {
			plugin.log(Level.INFO, "Error while handling message, ignoring player", t);
			ignore(iodinePlayer);
		}
	}
	
	private void ignore(@NotNull IodinePlayerImpl player) {
		player.setState(IodinePlayer.State.INVALID);
		player.getPlayer().sendMessage(ChatUtils.formatError("Invalid packet detected",
				"Mod features have been disabled for this session.",
				"If you believe this is a bug, please report it."));
	}
}
