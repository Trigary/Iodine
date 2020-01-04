package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.network.NetworkManagerImpl;
import hu.trigary.iodine.bukkit.player.PlayerManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The actual Bukkit plugin's main class.
 */
public class IodineBukkitPlugin extends JavaPlugin {
	private IodinePluginImpl plugin;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		plugin = new IodinePluginImpl(this);
		plugin.initialize(getConfig().getBoolean("debug-log"), getDescription().getVersion());
		plugin.onEnabled(new NetworkManagerImpl(plugin), new PlayerManagerImpl(plugin),
				Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId));
		
		Bukkit.getPluginManager().registerEvents(new TestCommandListener(), this);
	}
	
	@Override
	public void onDisable() {
		plugin.onDisabled();
	}
}
