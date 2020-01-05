package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Tha handler of {@link hu.trigary.iodine.backend.PacketType#SERVER_LOGIN_SUCCESS}.
 */
public class LoginSuccessPacketHandler extends PacketHandler {
	public LoginSuccessPacketHandler(@NotNull IodineMod mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull ByteBuffer buffer) {
		getMod().getLogger().info("Login > successful");
	}
}
