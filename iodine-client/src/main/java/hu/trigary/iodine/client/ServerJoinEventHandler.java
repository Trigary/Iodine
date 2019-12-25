package hu.trigary.iodine.client;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.PacketType;
import org.jetbrains.annotations.NotNull;

public abstract class ServerJoinEventHandler {
	private final IodineModBase mod;
	
	protected ServerJoinEventHandler(@NotNull IodineModBase mod) {
		this.mod = mod;
	}
	
	
	
	protected void handle() {
		mod.getLogger().info("Joined server, attempting login");
		byte[] version = BufferUtils.serializeString(mod.getVersion());
		mod.getNetwork().send(PacketType.CLIENT_LOGIN, version.length,
				b -> BufferUtils.serializeString(b, version));
	}
}
