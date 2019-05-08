package hu.trigary.iodine.forge.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class OutPacket extends BasePacket {
	@Override
	public final void fromBytes(ByteBuf buffer) {
		throw new AssertionError("OutPacket should never be deserialized");
	}
}
