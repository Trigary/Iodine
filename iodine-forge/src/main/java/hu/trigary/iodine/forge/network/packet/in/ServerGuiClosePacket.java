package hu.trigary.iodine.forge.network.packet.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.OutPacket;
import hu.trigary.iodine.forge.gui.IodineGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ServerGuiClosePacket extends InPacket {
	@Override
	protected void deserialize(ByteBuf buffer) { }
	
	public static class Handler extends InPacket.Handler<ServerGuiClosePacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerGuiClosePacket message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> {
				if (minecraft.currentScreen instanceof IodineGui) {
					mod.getLogger().info("Closing GUI");
					minecraft.player.closeScreen();
				} else {
					mod.getLogger().info("Can't close GUI: it's already closed");
				}
			});
			return null;
		}
	}
}
