package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The handler of a specific {@link hu.trigary.iodine.backend.PacketType}.
 */
public abstract class PacketHandler {
	private final IodineMod mod;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link hu.trigary.iodine.client.network.NetworkManager}.
	 *
	 * @param mod the mod instance
	 */
	protected PacketHandler(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	/**
	 * Gets the mod instance.
	 *
	 * @return the mod instance
	 */
	@NotNull
	@Contract(pure = true)
	protected final IodineMod getMod() {
		return mod;
	}
	
	/**
	 * Handles an incoming packet.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public abstract void handle(@NotNull InputBuffer buffer);
}
