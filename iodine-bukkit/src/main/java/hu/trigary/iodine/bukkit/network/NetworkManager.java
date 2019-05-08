package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.common.IodineConstants;
import hu.trigary.iodine.common.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

public class NetworkManager {
	private final IodinePlugin plugin = IodinePlugin.getInstance();
	private final byte[] oneBuffer = new byte[1];
	private final byte[] twoBuffer = new byte[2];
	
	public NetworkManager() {
		Messenger messenger = Bukkit.getMessenger();
		messenger.registerOutgoingPluginChannel(plugin, IodineConstants.NETWORK_CHANNEL);
		messenger.registerIncomingPluginChannel(plugin, IodineConstants.NETWORK_CHANNEL, new PacketListener());
	}
	
	
	
	public void send(Player player, PacketType type) {
		oneBuffer[0] = (byte) type.ordinal();
		send(player, oneBuffer);
	}
	
	public void send(Player player, PacketType type, byte value) {
		twoBuffer[0] = (byte) type.ordinal();
		twoBuffer[1] = value;
		send(player, twoBuffer);
	}
	
	public void send(Player player, byte[] message) {
		IodinePlugin.getInstance().getLogger().info("Sending message to " + player.getName()
				+ " of type " + PacketType.fromOrdinal(message[0]) + ", length: " + message.length);
		player.sendPluginMessage(plugin, IodineConstants.NETWORK_CHANNEL, message);
	}
}
