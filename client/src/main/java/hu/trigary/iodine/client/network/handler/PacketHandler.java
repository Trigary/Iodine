package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class PacketHandler {
	protected final IodineMod mod;
	
	protected PacketHandler(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	public abstract void handle(@NotNull ByteBuffer buffer);
}
