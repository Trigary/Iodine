package hu.trigary.iodine.bukkit.network;

import hu.trigary.iodine.bukkit.IodinePluginImpl;
import hu.trigary.iodine.server.network.PacketListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link PacketListener}.
 */
public class PacketListenerImpl extends PacketListener implements PluginMessageListener {
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link NetworkManagerImpl}.
	 *
	 * @param plugin the plugin instance
	 */
	public PacketListenerImpl(@NotNull IodinePluginImpl plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
		onMessageReceived(player.getUniqueId(), message);
	}
}
