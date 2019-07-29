package hu.trigary.iodine.forge.network.in;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.out.OutPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class ServerLoginFailedPacket extends InPacket {
	private boolean outdatedClient;
	
	@Override
	public void deserialize(ByteBuf buffer) {
		outdatedClient = buffer.readByte() == 0;
	}
	
	public static class Handler extends InPacket.Handler<ServerLoginFailedPacket> {
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		protected OutPacket handle(ServerLoginFailedPacket message) {
			mod.getLogger().info("Server rejected login: outdated "
					+ (message.outdatedClient ? "client" : "server"));
			
			String text = message.outdatedClient
					? "You have an outdated version of Iodine installed. "
					+ "Iodine will not work, you must update."
					: "The server has an outdated version of Iodine installed. "
					+ "Iodine will not work for you, ask the server staff to update.";
			
			text = TextFormatting.GRAY + "[" + TextFormatting.YELLOW + "Iodine"
					+ TextFormatting.GRAY + "] " + TextFormatting.RED + text;
			TextComponentString textComponent = new TextComponentString(text);
			
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(() -> minecraft.player.sendMessage(textComponent));
			return null;
		}
	}
}
