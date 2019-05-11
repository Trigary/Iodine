package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.BasePacket;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public abstract class InPacket extends BasePacket {
	@Override
	public final void serialize(ByteBuf buffer) {
		throw new AssertionError("InPacket should never be serialized");
	}
	
	public abstract static class Handler<REQ extends InPacket> implements IMessageHandler<REQ, OutPacket> {
		protected final IodineMod mod;
		
		protected Handler(@NotNull IodineMod mod) {
			this.mod = mod;
		}
		
		@Override
		public final OutPacket onMessage(REQ message, MessageContext context) {
			return handle(message);
		}
		
		protected abstract OutPacket handle(REQ message);
	}
}
