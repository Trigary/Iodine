package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class PacketHandler {
	private final IodineMod mod;
	
	protected PacketHandler(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	protected final IodineMod getMod() {
		return mod;
	}
	
	public abstract void handle(@NotNull ByteBuffer buffer);
}
