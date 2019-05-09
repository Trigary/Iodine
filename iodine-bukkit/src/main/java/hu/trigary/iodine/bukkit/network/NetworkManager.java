package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.backend.IodineConstants;
import hu.trigary.iodine.backend.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class NetworkManager {
	private final IodinePlugin plugin;
	
	public NetworkManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		Messenger messenger = Bukkit.getMessenger();
		String channel = IodineConstants.NETWORK_CHANNEL;
		messenger.registerOutgoingPluginChannel(plugin, channel);
		messenger.registerIncomingPluginChannel(plugin, channel, new PacketListener(plugin));
	}
	
	
	
	public void send(@NotNull Player player, @NotNull PacketType type) {
		send(player, new byte[]{(byte) type.ordinal()});
	}
	
	public void send(@NotNull Player player, @NotNull PacketType type, byte value) {
		send(player, new byte[]{(byte) type.ordinal(), value});
	}
	
	public void send(@NotNull Player player, @NotNull PacketType type, int value) {
		send(player, type, 4, buffer -> buffer.putInt(value));
	}
	
	public void send(@NotNull Player player, @NotNull PacketType type,
			int contentsLength, @NotNull Consumer<ByteBuffer> contentProvider) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[contentsLength + 1]);
		buffer.put((byte) type.ordinal());
		contentProvider.accept(buffer);
		send(player, buffer.array());
	}
	
	private void send(@NotNull Player player, @NotNull byte[] message) {
		plugin.logDebug("Sending message of type {0} to {1}", PacketType.fromOrdinal(message[0]), player.getName());
		player.sendPluginMessage(plugin, IodineConstants.NETWORK_CHANNEL, message);
	}
}
