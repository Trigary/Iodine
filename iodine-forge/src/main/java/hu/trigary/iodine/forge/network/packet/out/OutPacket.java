package hu.trigary.iodine.forge.network.packet.out;

import hu.trigary.iodine.forge.network.packet.BasePacket;
import io.netty.buffer.ByteBuf;

public abstract class OutPacket extends BasePacket {
	@Override
	public final void deserialize(ByteBuf buffer) {
		throw new AssertionError("OutPacket should never be deserialized");
	}
}
