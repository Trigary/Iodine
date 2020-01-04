package hu.trigary.iodine.server.network;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.backend.ChatUtils;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.network.handler.GuiChangePacketHandler;
import hu.trigary.iodine.server.network.handler.GuiClosePacketHandler;
import hu.trigary.iodine.server.network.handler.LoginPacketHandler;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.server.network.handler.PacketHandler;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A class which listens to incoming messages and handles them,
 * delegating valid messages to the internally registered {@link PacketHandler} instances.
 */
public abstract class PacketListener {
	private final IodinePlugin plugin;
	private final PacketHandler[] handlers = new PacketHandler[PacketType.getHighestId() + 1];
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link NetworkManager}.
	 *
	 * @param plugin the plugin instance
	 */
	protected PacketListener(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		handlers[PacketType.CLIENT_LOGIN.getUnsignedId()] = new LoginPacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CLOSE.getUnsignedId()] = new GuiClosePacketHandler(plugin);
		handlers[PacketType.CLIENT_GUI_CHANGE.getUnsignedId()] = new GuiChangePacketHandler(plugin);
	}
	
	
	
	//TODO docs
	protected final void onMessageReceived(@NotNull UUID player, @NotNull byte[] message) {
		IodinePlayerBase iodinePlayer = plugin.getPlayer(player);
		IodinePlayer.State state = iodinePlayer.getState();
		if (state == IodinePlayer.State.INVALID) {
			plugin.log(Level.OFF, "Network > ignoring message from invalid player {0}", iodinePlayer.getName());
			return;
		}
		
		if (message.length == 0) {
			plugin.log(Level.INFO, "Network > received empty message from {0}, ignoring player", iodinePlayer.getName());
			ignore(iodinePlayer);
			return;
		}
		
		PacketType type = PacketType.fromId(message[0]);
		if (type == null) {
			plugin.log(Level.INFO, "Network > received message with invalid type-id ({0}) from {1}, ignoring player",
					message[0], iodinePlayer.getName());
			ignore(iodinePlayer);
			return;
		}
		
		PacketHandler handler = handlers[type.getUnsignedId()];
		if (handler == null) {
			plugin.log(Level.INFO, "Network > received message with invalid type {0} from {1}, ignoring player",
					type, iodinePlayer.getName());
			ignore(iodinePlayer);
			return;
		}
		
		if (handler.getTargetState() != state) {
			plugin.log(Level.INFO, "Network > received {0} from {1} in (incorrect) state: {2}",
					type, iodinePlayer.getName(), state);
			return;
		}
		
		try {
			plugin.log(Level.OFF, "Network > handling {0} from {1}", type, iodinePlayer.getName());
			handler.handle(iodinePlayer, ByteBuffer.wrap(message, 1, message.length - 1));
		} catch (Throwable t) {
			plugin.log(Level.INFO, "Network > error handling {0} from {1}, ignoring player", type, iodinePlayer.getName());
			plugin.log(Level.INFO, "Network > cause (this is maybe a bug, maybe not):", t);
			ignore(iodinePlayer);
		}
	}
	
	private void ignore(@NotNull IodinePlayerBase player) {
		player.setState(IodinePlayer.State.INVALID);
		player.sendMessage(ChatUtils.formatError("Invalid packet detected",
				"Mod features have been disabled for this session.",
				"If you believe this is a bug, please report it."));
	}
}
