package hu.trigary.iodine.forge.network.packet.out;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ClientGuiChangePacket extends GuiOutPacket { //TODO
	private final int elementId;
	private final byte[] contents;
	
	public ClientGuiChangePacket(int guiId, int elementId, @NotNull byte[] contents) {
		super(guiId);
		this.elementId = elementId;
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.contents = contents;
	}
	
	@Override
	protected void serializeInner(ByteBuf buffer) {
		buffer.writeInt(elementId);
		buffer.writeInt(contents.length);
		buffer.writeBytes(contents);
	}
}
