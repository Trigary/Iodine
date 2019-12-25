package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class LoginSuccessPacketHandler extends PacketHandler {
	public LoginSuccessPacketHandler(@NotNull IodineModBase mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		mod.getLogger().info("Sever accepted login");
	}
}
