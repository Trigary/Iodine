package hu.trigary.iodine.forge.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class InPacket extends BasePacket {
	@Override
	public final void toBytes(ByteBuf buffer) {
		throw new AssertionError("InPacket should never be serialized");
	}
}
