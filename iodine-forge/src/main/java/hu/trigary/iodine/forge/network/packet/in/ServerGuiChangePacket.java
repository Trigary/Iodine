package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ServerGuiChangePacket extends GuiInPacket {
	private int elementId;
	private byte[] contents;
	
	@Override
	protected void deserializeInner(ByteBuf buffer) {
		elementId = buffer.readInt();
		int length = buffer.readInt();
		contents = new byte[buffer.readInt()];
		buffer.readBytes(contents, 0, length);
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiChangePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected void handle(ServerGuiChangePacket message) {
			//TODO
		}
	}
}
