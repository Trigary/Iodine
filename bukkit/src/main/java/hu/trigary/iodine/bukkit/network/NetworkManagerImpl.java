package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePluginImpl;
import hu.trigary.iodine.server.network.NetworkManager;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;

public class NetworkManagerImpl extends NetworkManager {
	private final JavaPlugin plugin;
	
	public NetworkManagerImpl(@NotNull IodinePluginImpl plugin) {
		super(plugin);
		this.plugin = plugin.getBukkitPlugin();
		Messenger messenger = Bukkit.getMessenger();
		String channel = PacketType.NETWORK_CHANNEL;
		messenger.registerOutgoingPluginChannel(this.plugin, channel);
		messenger.registerIncomingPluginChannel(this.plugin, channel, new PacketListenerImpl(plugin));
	}
	
	
	
	@Override
	protected void sendImpl(@NotNull IodinePlayerBase player, @NotNull byte[] message) {
		player.getPlayer(Player.class).sendPluginMessage(plugin, PacketType.NETWORK_CHANNEL, message);
	}
}
