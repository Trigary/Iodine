package hu.trigary.iodine.forge.network.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.out.ClientLoginPacket;
import hu.trigary.iodine.forge.network.out.OutPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ServerLoginRequest extends InPacket {
	@Override
	protected void deserialize(ByteBuf buffer) {}
	
	public static class Handler extends InPacket.Handler<ServerLoginRequest> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerLoginRequest message) {
			return new ClientLoginPacket(mod.getVersion());
		}
	}
}
