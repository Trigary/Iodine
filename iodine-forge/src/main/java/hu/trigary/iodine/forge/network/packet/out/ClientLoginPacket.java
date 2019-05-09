package hu.trigary.iodine.forge.network.packet.out;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class ClientLoginPacket extends OutPacket {
	private final String version;
	
	public ClientLoginPacket(@NotNull String version) {
		this.version = version;
	}
	
	@Override
	protected void serialize(ByteBuf buffer) {
		buffer.writeCharSequence(version, Charsets.UTF_8);
	}
}
