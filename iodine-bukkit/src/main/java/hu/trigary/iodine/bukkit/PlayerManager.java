package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.PlayerState;
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
	private final Map<UUID, PlayerState> players = new HashMap<>();
	
	public PlayerManager(@NotNull IodinePlugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public PlayerState getState(@NotNull Player player) {
		return players.getOrDefault(player.getUniqueId(), PlayerState.VANILLA);
	}
	
	public void setState(@NotNull Player player, @NotNull PlayerState state) {
		players.put(player.getUniqueId(), state);
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer().getUniqueId());
	}
}
