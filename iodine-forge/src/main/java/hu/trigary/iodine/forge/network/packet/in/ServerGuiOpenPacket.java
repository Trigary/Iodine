package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.ui.IodineGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ServerGuiOpenPacket extends GuiInPacket {
	@Override
	protected void deserializeInner(ByteBuf buffer) {
		//TODO
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiOpenPacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected void handle(ServerGuiOpenPacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> minecraft.displayGuiScreen(new IodineGui(mod, message.guiId)));
		}
	}
}
