package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.ui.IodineGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class ServerGuiClosePacket extends GuiInPacket {
	@Override
	protected void deserializeInner(ByteBuf buffer) { }
	
	public static class Handler extends InPacket.Handler<ServerGuiClosePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected void handle(ServerGuiClosePacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> {
				GuiScreen current = minecraft.currentScreen;
				if (current instanceof IodineGui && ((IodineGui) current).getId() == message.guiId) {
					minecraft.player.closeScreen();
				}
			});
		}
	}
}
