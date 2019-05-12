package hu.trigary.iodine.bukkit.player;

import hu.trigary.iodine.bukkit.IodinePlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The manager whose responsibility is linking {@link Player}
 * and {@link IodinePlayerImpl} instances, without causing memory leaks.
 */
public class PlayerManager implements Listener {
	private final Map<UUID, IodinePlayerImpl> players = new HashMap<>();
	private final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 * Internally registers the created instance as a Bukkit event listener.
	 *
	 * @param plugin the plugin instance
	 */
	public PlayerManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getOnlinePlayers().forEach(p -> onPlayerJoin(new PlayerJoinEvent(p, null)));
	}
	
	
	
	/**
	 * Gets the data associated with the specified player.
	 *
	 * @param player the player to get data about
	 * @return the data associated with the player
	 */
	@NotNull
	@Contract(pure = true)
	public IodinePlayerImpl getPlayer(@NotNull Player player) {
		IodinePlayerImpl impl = players.get(player.getUniqueId());
		Validate.notNull(impl, "IodinePlayer instances only exist for online players");
		return impl;
	}
	
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer().getUniqueId(), new IodinePlayerImpl(plugin, event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		IodinePlayerImpl player = players.get(event.getPlayer().getUniqueId());
		//can't call remove yet: the gui close callback might still require the instance
		if (player.getOpenGui() != null) {
			player.getOpenGui().closeForNoPacket(player);
		}
		players.remove(event.getPlayer().getUniqueId());
	}
}
