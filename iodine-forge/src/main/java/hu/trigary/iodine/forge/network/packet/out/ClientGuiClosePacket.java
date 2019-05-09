package hu.trigary.iodine.forge.network.packet.out;

import io.netty.buffer.ByteBuf;

public class ClientGuiClosePacket extends GuiOutPacket {
	
	public ClientGuiClosePacket(int guiId) {
		super(guiId);
	}
	
	@Override
	protected void serializeInner(ByteBuf buffer) {
	
	}
}
