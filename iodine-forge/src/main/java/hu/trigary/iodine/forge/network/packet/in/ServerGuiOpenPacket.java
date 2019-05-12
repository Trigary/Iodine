package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import hu.trigary.iodine.forge.gui.IodineGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ServerGuiOpenPacket extends InPacket {
	private int guiId;
	private byte[] data;
	
	@Override
	protected void deserialize(ByteBuf buffer) {
		guiId = buffer.readInt();
		data = new byte[buffer.readableBytes()];
		buffer.readBytes(data);
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiOpenPacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerGuiOpenPacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> {
				mod.getLogger().info("Opening GUI");
				minecraft.displayGuiScreen(new IodineGui(mod, message.guiId, message.data));
			});
			return null;
		}
	}
}
