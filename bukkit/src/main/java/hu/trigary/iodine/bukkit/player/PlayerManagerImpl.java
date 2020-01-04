package hu.trigary.iodine.bukkit.player;

import hu.trigary.iodine.bukkit.IodinePluginImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import hu.trigary.iodine.server.player.PlayerManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerManagerImpl extends PlayerManager {
	private final IodinePluginImpl plugin;
	
	public PlayerManagerImpl(@NotNull IodinePluginImpl plugin) {
		super(plugin);
		this.plugin = plugin;
	}
	
	
	
	@NotNull
	@Override
	protected IodinePlayerBase tryCreatePlayer(@NotNull UUID player) {
		Player bukkitPlayer = Bukkit.getPlayer(player);
		Validate.notNull(bukkitPlayer, "IodinePlayer instances only exist for online players");
		return new IodinePlayerImpl(plugin, bukkitPlayer);
	}
}
