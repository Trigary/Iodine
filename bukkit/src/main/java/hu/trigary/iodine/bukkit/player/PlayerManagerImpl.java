package hu.trigary.iodine.bukkit.player;

import hu.trigary.iodine.bukkit.IodinePluginImpl;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import hu.trigary.iodine.server.player.PlayerManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The implementation of {@link PlayerManager}.
 */
public class PlayerManagerImpl extends PlayerManager implements Listener {
	private final IodinePluginImpl plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by the {@link IodinePlugin} implementation.
	 *
	 * @param plugin the plugin instance
	 */
	public PlayerManagerImpl(@NotNull IodinePluginImpl plugin) {
		super(plugin);
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin.getBukkitPlugin());
	}
	
	
	
	@NotNull
	@Override
	protected IodinePlayerBase tryCreatePlayer(@NotNull UUID player) {
		Player bukkitPlayer = Bukkit.getPlayer(player);
		Validate.notNull(bukkitPlayer, "IodinePlayer instances only exist for online players");
		return new IodinePlayerImpl(plugin, bukkitPlayer);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
		onPlayerQuit(event.getPlayer().getUniqueId());
	}
}
