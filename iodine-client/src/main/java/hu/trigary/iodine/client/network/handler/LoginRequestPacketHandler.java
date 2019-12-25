package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class LoginRequestPacketHandler extends PacketHandler {
	public LoginRequestPacketHandler(@NotNull IodineModBase mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		byte[] version = BufferUtils.serializeString(mod.getVersion());
		mod.getNetwork().send(PacketType.CLIENT_LOGIN, version.length,
				b -> BufferUtils.serializeString(b, version));
	}
}
