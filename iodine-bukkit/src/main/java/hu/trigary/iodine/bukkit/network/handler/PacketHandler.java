package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.api.PlayerState;
import hu.trigary.iodine.bukkit.IodinePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class PacketHandler {
	protected final IodinePlugin plugin;
	private final PlayerState targetState;
	
	protected PacketHandler(@NotNull IodinePlugin plugin, @NotNull PlayerState targetState) {
		this.plugin = plugin;
		this.targetState = targetState;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final PlayerState getTargetState() {
		return targetState;
	}
	
	public abstract void handle(@NotNull Player player, @NotNull byte[] message);
}
