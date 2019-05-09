package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.api.player.IodinePlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager implements Listener {
	private final Map<UUID, IodinePlayerImpl> players = new HashMap<>();
	private final IodinePlugin plugin;
	
	public PlayerManager(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public IodinePlayerImpl getPlayer(@NotNull Player player) {
		return players.computeIfAbsent(player.getUniqueId(),
				k -> new IodinePlayerImpl(plugin, player));
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		IodinePlayerImpl impl = players.remove(event.getPlayer().getUniqueId());
		if (impl != null) {
			impl.closeGui();
		}
	}
}
