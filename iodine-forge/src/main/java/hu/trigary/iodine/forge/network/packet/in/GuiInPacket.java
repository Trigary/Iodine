package hu.trigary.iodine.forge.network.packet.in;

import io.netty.buffer.ByteBuf;

public abstract class GuiInPacket extends InPacket {
	protected int guiId;
	
	@Override
	protected final void deserialize(ByteBuf buffer) {
		guiId = buffer.readInt();
		deserializeInner(buffer);
	}
	
	protected abstract void deserializeInner(ByteBuf buffer);
}
