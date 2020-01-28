package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

/**
 * Tha handler of {@link hu.trigary.iodine.backend.PacketType#SERVER_LOGIN_FAILED}.
 */
public class LoginFailedPacketHandler extends PacketHandler {
	public LoginFailedPacketHandler(@NotNull IodineMod mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull InputBuffer buffer) {
		int value = buffer.readByte();
		if (value == 0) {
			getMod().getLogger().info("Login > client sent invalid login packet");
		} else if (value == 1) {
			getMod().getLogger().info("Login > outdated client");
		} else if (value == 2) {
			getMod().getLogger().info("Login > outdated server");
		} else {
			getMod().getLogger().info("Login > unknown reason, probably version mismatch");
		}
	}
}
