package hu.trigary.iodine.forge.network.out;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class ClientLoginPacket extends OutPacket {
	private final String version;
	
	public ClientLoginPacket(@NotNull String version) {
		this.version = version;
	}
	
	@Override
	protected void serialize(ByteBuf buffer) {
		buffer.writeCharSequence(version, StandardCharsets.UTF_8);
	}
}
