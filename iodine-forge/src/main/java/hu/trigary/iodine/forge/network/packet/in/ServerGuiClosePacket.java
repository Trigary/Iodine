package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import hu.trigary.iodine.forge.ui.IodineGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class ServerGuiClosePacket extends InPacket {
	private int guiId;
	
	@Override
	protected void deserialize(ByteBuf buffer) {
		guiId = buffer.readInt();
	}
	
	public static class Handler extends InPacket.Handler<ServerGuiClosePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerGuiClosePacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> {
				GuiScreen screen = minecraft.currentScreen;
				if (!(screen instanceof IodineGui)) {
					return;
				}
				
				IodineGui gui = (IodineGui) screen;
				if (gui.getId() == message.guiId) {
					gui.flagServerClosing();
					minecraft.player.closeScreen();
				}
			});
			return null;
		}
	}
}
