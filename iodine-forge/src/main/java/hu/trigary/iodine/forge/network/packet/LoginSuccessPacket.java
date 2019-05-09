package hu.trigary.iodine.forge.network.packet;

import hu.trigary.iodine.forge.IodineMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class LoginSuccessPacket extends InPacket {
	@Override
	public void fromBytes(ByteBuf buf) { }
	
	public static class Handler extends InPacket.Handler<LoginSuccessPacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		public OutPacket onMessage(LoginSuccessPacket message, MessageContext context) {
			mod.getLogger().info("Server accepted login");
			return null;
		}
	}
}
