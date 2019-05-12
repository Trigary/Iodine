package hu.trigary.iodine.forge.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class BasePacket implements IMessage {
	@Override
	public final void fromBytes(ByteBuf buffer) {
		deserialize(buffer);
	}
	
	protected abstract void deserialize(ByteBuf buffer);
	
	@Override
	public final void toBytes(ByteBuf buffer) {
		serialize(buffer);
	}
	
	protected abstract void serialize(ByteBuf buffer);
}
