package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.PlayerState;
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

public class PlayerManager implements Listener {
	private static final long LOGIN_WINDOW_MILLIS = 1000;
	private final Map<UUID, PlayerData> players = new HashMap<>();
	
	public PlayerManager(@NotNull IodinePlugin plugin) {
		Bukkit.getOnlinePlayers().forEach(p -> onPlayerJoin(new PlayerJoinEvent(p, null)));
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public PlayerState getState(@NotNull Player player) {
		PlayerData data = players.get(player.getUniqueId());
		if (data == null) {
			return PlayerState.UNKNOWN; //if the player is not online anymore
		}
		
		if (data.state == PlayerState.UNKNOWN) {
			long current = System.currentTimeMillis();
			if (current - data.joinedTimestamp > LOGIN_WINDOW_MILLIS) {
				data.state = PlayerState.VANILLA;
			}
		}
		
		return data.state;
	}
	
	public void setState(@NotNull Player player, @NotNull PlayerState state) {
		PlayerData data = players.get(player.getUniqueId());
		if (data != null) { //null if the player is not online anymore
			data.state = state;
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer().getUniqueId(), new PlayerData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer().getUniqueId());
	}
	
	private static class PlayerData {
		PlayerState state = PlayerState.UNKNOWN;
		final long joinedTimestamp = System.currentTimeMillis();
	}
}
