package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.common.IodineConstants;
import hu.trigary.iodine.common.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

public class NetworkManager {
	private final IodinePlugin plugin;
	private final byte[] oneBuffer = new byte[1];
	private final byte[] twoBuffer = new byte[2];
	
	public NetworkManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		Messenger messenger = Bukkit.getMessenger();
		String channel = IodineConstants.NETWORK_CHANNEL;
		messenger.registerOutgoingPluginChannel(plugin, channel);
		messenger.registerIncomingPluginChannel(plugin, channel, new PacketListener(plugin));
	}
	
	
	
	public void send(@NotNull Player player, @NotNull PacketType type) {
		oneBuffer[0] = (byte) type.ordinal();
		send(player, oneBuffer);
	}
	
	public void send(@NotNull Player player, @NotNull PacketType type, byte value) {
		twoBuffer[0] = (byte) type.ordinal();
		twoBuffer[1] = value;
		send(player, twoBuffer);
	}
	
	public void send(@NotNull Player player, @NotNull byte[] message) {
		plugin.logDebug("Sending message of type {0} to {1}", PacketType.fromOrdinal(message[0]), player.getName());
		player.sendPluginMessage(plugin, IodineConstants.NETWORK_CHANNEL, message);
	}
}
