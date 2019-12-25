package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class GuiOpenPacketHandler extends PacketHandler {
	public GuiOpenPacketHandler(@NotNull IodineModBase mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		mod.getGui().openGui(buffer);
	}
}
