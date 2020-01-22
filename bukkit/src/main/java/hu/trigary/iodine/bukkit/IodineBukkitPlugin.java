package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.network.NetworkManagerImpl;
import hu.trigary.iodine.bukkit.player.PlayerManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The actual Bukkit plugin's main class.
 */
public class IodineBukkitPlugin extends JavaPlugin implements Listener {
	private IodinePluginImpl plugin;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		plugin = new IodinePluginImpl(this);
		plugin.initialize(getLogger(), getConfig().getBoolean("debug-log"), getDescription().getVersion());
		plugin.onEnabled(new NetworkManagerImpl(plugin), new PlayerManagerImpl(plugin),
				Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId));
		
		Bukkit.getPluginManager().registerEvents(new TestCommandListener(), this);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	private void onDisabled(@NotNull PluginDisableEvent event) {
		if (event.getPlugin() == this) {
			plugin.onDisabled();
		}
	}
}
