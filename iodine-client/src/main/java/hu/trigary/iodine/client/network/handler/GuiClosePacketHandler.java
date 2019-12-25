package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class GuiClosePacketHandler extends PacketHandler {
	public GuiClosePacketHandler(@NotNull IodineModBase mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		mod.getGui().closeGui();
	}
}
