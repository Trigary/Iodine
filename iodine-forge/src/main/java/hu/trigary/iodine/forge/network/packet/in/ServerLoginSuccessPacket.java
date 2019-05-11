package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ServerLoginSuccessPacket extends InPacket {
	@Override
	public void deserialize(ByteBuf buf) { }
	
	public static class Handler extends InPacket.Handler<ServerLoginSuccessPacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerLoginSuccessPacket message) {
			mod.getLogger().info("Server accepted login");
			return null;
		}
	}
}
