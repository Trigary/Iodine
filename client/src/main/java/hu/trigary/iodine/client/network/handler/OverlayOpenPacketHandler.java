package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class OverlayOpenPacketHandler extends PacketHandler {
	public OverlayOpenPacketHandler(@NotNull IodineMod mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		getMod().getOverlayManager().packetOpenOverlay(buffer);
	}
}
