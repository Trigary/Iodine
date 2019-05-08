package hu.trigary.iodine.forge.network.packet;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

public class LoginPacket extends OutPacket {
	private final String version;
	
	public LoginPacket(String version) {
		this.version = version;
	}
	
	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeCharSequence(version, Charsets.UTF_8);
	}
}
