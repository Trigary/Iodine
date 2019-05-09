package hu.trigary.iodine.forge.network.packet;

import hu.trigary.iodine.forge.IodineMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import org.jetbrains.annotations.NotNull;

public abstract class InPacket extends BasePacket {
	@Override
	public final void toBytes(ByteBuf buffer) {
		throw new AssertionError("InPacket should never be serialized");
	}
	
	public abstract static class Handler<REQ extends InPacket> implements IMessageHandler<REQ, OutPacket> {
		protected final IodineMod mod;
		
		protected Handler(@NotNull IodineMod mod) {
			this.mod = mod;
		}
	}
}
