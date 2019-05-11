package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.api.player.PlayerState;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.backend.PacketType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * A class that is able to handle a specific {@link PacketType}.
 */
public abstract class PacketHandler {
	protected final IodinePlugin plugin;
	
	protected PacketHandler(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	/**
	 * Gets the state that players must be in when they send a
	 * message of {@link PacketType} associated with this handler.
	 * If the returned value does not match the sender's state,
	 * then the sender will become {@link PlayerState#INVALID}.
	 *
	 * @return the valid state of the sender
	 */
	@NotNull
	@Contract(pure = true)
	public PlayerState getTargetState() {
		return PlayerState.MODDED;
	}
	
	/**
	 * Handles an incoming packet.
	 * If an exception is thrown by this method,
	 * then the sender becomes {@link PlayerState#INVALID}.
	 *
	 * @param player the sender of the packet
	 * @param message the payload of the packet
	 */
	public abstract void handle(@NotNull Player player, @NotNull ByteBuffer message);
}
