package hu.trigary.iodine.forge.network.out;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ClientGuiChangePacket extends OutPacket {
	private final int guiId;
	private final byte[] data;
	
	public ClientGuiChangePacket(int guiId, @NotNull byte[] data) {
		this.guiId = guiId;
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.data = data;
	}
	
	@Override
	protected void serialize(ByteBuf buffer) {
		buffer.writeInt(guiId);
		buffer.writeInt(data.length);
		buffer.writeBytes(data);
	}
}
