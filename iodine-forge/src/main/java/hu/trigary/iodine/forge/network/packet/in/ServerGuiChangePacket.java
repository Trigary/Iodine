package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class ServerGuiChangePacket extends InPacket {
	private byte[] data;
	
	@Override
	protected void deserialize(ByteBuf buffer) {
		data = new byte[buffer.readableBytes()];
		buffer.readBytes(data);
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiChangePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerGuiChangePacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> {
				GuiScreen screen = minecraft.currentScreen;
				if (screen instanceof IodineGui) {
					mod.getLogger().info("Updating GUI");
					((IodineGui) screen).deserialize(message.data);
				} else {
					mod.getLogger().info("Can't update GUI: it's no longer opened");
				}
			});
			return null;
		}
	}
}
