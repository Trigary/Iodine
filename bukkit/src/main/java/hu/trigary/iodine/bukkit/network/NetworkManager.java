package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.backend.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * The manager which sets up the messaging channel and allows packets to be sent.
 */
public class NetworkManager {
	private final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public NetworkManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		Messenger messenger = Bukkit.getMessenger();
		String channel = PacketType.NETWORK_CHANNEL;
		messenger.registerOutgoingPluginChannel(plugin, channel);
		messenger.registerIncomingPluginChannel(plugin, channel, new PacketListener(plugin));
	}
	
	
	
	/**
	 * Sends an empty packet of the specified type to the specified client.
	 *
	 * @param player the recipient
	 * @param type the type of the packet
	 */
	public void send(@NotNull Player player, @NotNull PacketType type) {
		send(player, new byte[]{type.getId()});
	}
	
	/**
	 * Sends a packet of the specified type to the specified client,
	 * with a single byte as its payload.
	 *
	 * @param player the recipient
	 * @param type the type of the packet
	 * @param value the payload of the packet
	 */
	public void send(@NotNull Player player, @NotNull PacketType type, byte value) {
		send(player, new byte[]{type.getId(), value});
	}
	
	/**
	 * Sends a packet of the specified type to the specified client.
	 * The payload is set by a {@link Consumer}, which can put
	 * {@code dataLength} many bytes into the passed {@link ByteBuffer}.
	 *
	 * @param player the recipient
	 * @param type the type of the packet
	 * @param dataLength the length of the payload, in bytes
	 * @param dataProvider the action which sets the payload
	 */
	public void send(@NotNull Player player, @NotNull PacketType type,
			int dataLength, @NotNull Consumer<ByteBuffer> dataProvider) {
		byte[] message = new byte[dataLength + 1];
		message[0] = type.getId();
		dataProvider.accept(ByteBuffer.wrap(message, 1, dataLength));
		send(player, message);
	}
	
	/**
	 * Sends the specified payload to the specified client.
	 * The payload's first byte must be a {@link PacketType} ID.
	 *
	 * @param player the recipient
	 * @param message the payload
	 */
	public void send(@NotNull Player player, @NotNull byte[] message) {
		plugin.log(Level.OFF, "Sending message of type {0} to {1}", PacketType.fromId(message[0]), player.getName());
		player.sendPluginMessage(plugin, PacketType.NETWORK_CHANNEL, message);
	}
}
