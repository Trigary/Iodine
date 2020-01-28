package hu.trigary.iodine.client.network.handler;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

/**
 * Tha handler of {@link hu.trigary.iodine.backend.PacketType#SERVER_LOGIN_REQUEST}.
 */
public class LoginRequestPacketHandler extends PacketHandler {
	public LoginRequestPacketHandler(@NotNull IodineMod mod) {
		super(mod);
	}
	
	@Override
	public void handle(@NotNull InputBuffer buffer) {
		getMod().getLogger().debug("Login > responding to server request");
		getMod().getNetworkManager().send(PacketType.CLIENT_LOGIN, b -> b.putString(getMod().getVersion()));
	}
}
