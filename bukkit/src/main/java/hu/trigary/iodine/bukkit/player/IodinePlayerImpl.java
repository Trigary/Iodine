package hu.trigary.iodine.bukkit.player;

import hu.trigary.iodine.bukkit.IodinePluginImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IodinePlayerImpl extends IodinePlayerBase {
	private final Player player;
	
	public IodinePlayerImpl(@NotNull IodinePluginImpl plugin, @NotNull Player player) {
		super(plugin);
		this.player = player;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public UUID getPlayer() {
		return player.getUniqueId();
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public <T> T getPlayer(@NotNull Class<T> clazz) {
		return clazz.cast(player);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getName() {
		return player.getName();
	}
	
	
	
	@Override
	public void sendMessage(@NotNull String message) {
		player.sendMessage(message);
	}
}
