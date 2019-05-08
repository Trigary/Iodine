package hu.trigary.iodine.forge.network.packet;

import hu.trigary.iodine.forge.IodineMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoginSuccessPacket extends InPacket {
	@Override
	public void fromBytes(ByteBuf buf) { }
	
	public static class Handler implements IMessageHandler<LoginSuccessPacket, OutPacket> {
		@Override
		public OutPacket onMessage(LoginSuccessPacket message, MessageContext context) {
			IodineMod.getInstance().getLogger().info("Server accepted login");
			return null;
		}
	}
}
