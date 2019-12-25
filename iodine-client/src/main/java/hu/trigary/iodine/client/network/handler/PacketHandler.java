package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class PacketHandler {
	protected final IodineModBase mod;
	
	protected PacketHandler(@NotNull IodineModBase mod) {
		this.mod = mod;
	}
	
	
	
	public abstract void handle(@NotNull ByteBuffer buffer);
}
