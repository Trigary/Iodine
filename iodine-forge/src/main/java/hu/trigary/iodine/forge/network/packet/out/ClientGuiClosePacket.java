package hu.trigary.iodine.forge.network.packet.out;

import io.netty.buffer.ByteBuf;

public class ClientGuiClosePacket extends OutPacket {
	private final int guiId;
	
	public ClientGuiClosePacket(int guiId) {
		this.guiId = guiId;
	}
	
	@Override
	protected void serialize(ByteBuf buffer) {
		buffer.writeInt(guiId);
	}
}
