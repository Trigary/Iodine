package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ServerGuiChangePacket extends InPacket {
	private int guiId;
	private byte[] data;
	
	@Override
	protected void deserialize(ByteBuf buffer) {
		guiId = buffer.readInt();
		data = new byte[buffer.readableBytes()];
		buffer.readBytes(data);
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiChangePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerGuiChangePacket message) {
			mod.getLogger().info("Received change data: " + Arrays.toString(message.data));
			//TODO use data
			return null;
		}
	}
}
