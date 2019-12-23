package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * A class that is able to handle a specific {@link PacketType}.
 */
public abstract class PacketHandler {
	protected final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link PacketListener}.
	 *
	 * @param plugin the plugin instance
	 */
	protected PacketHandler(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	/**
	 * Gets the state that players must be in when they send a
	 * message of {@link PacketType} associated with this handler.
	 *
	 * @return the valid state of the sender
	 */
	@NotNull
	@Contract(pure = true)
	public IodinePlayer.State getTargetState() {
		return IodinePlayer.State.MODDED;
	}
	
	/**
	 * Handles an incoming packet.
	 * If an exception is thrown by this method,
	 * then the sender becomes {@link IodinePlayer.State#INVALID}.
	 *
	 * @param player the sender of the packet
	 * @param message the payload of the packet
	 */
	public abstract void handle(@NotNull IodinePlayerImpl player, @NotNull ByteBuffer message);
}
