package hu.trigary.iodine.forge.network.packet.out;

import io.netty.buffer.ByteBuf;

public abstract class GuiOutPacket extends OutPacket {
	protected final int guiId;
	
	protected GuiOutPacket(int guiId) {
		this.guiId = guiId;
	}
	
	@Override
	protected void serialize(ByteBuf buffer) {
		buffer.writeInt(guiId);
		serializeInner(buffer);
	}
	
	protected abstract void serializeInner(ByteBuf buffer);
}
