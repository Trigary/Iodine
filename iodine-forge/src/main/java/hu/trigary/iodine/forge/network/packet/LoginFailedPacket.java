package hu.trigary.iodine.forge.network.packet;

import hu.trigary.iodine.forge.IodineMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jetbrains.annotations.NotNull;

public class LoginFailedPacket extends InPacket {
	private boolean outdatedClient;
	
	@Override
	public void fromBytes(ByteBuf buf) {
		outdatedClient = buf.readByte() == 0;
	}
	
	public static class Handler extends InPacket.Handler<LoginFailedPacket> {
		
		public Handler(@NotNull IodineMod mod) {
			super(mod);
		}
		
		@Override
		public OutPacket onMessage(LoginFailedPacket message, MessageContext context) {
			mod.getLogger().info("Server rejected login: outdated "
					+ (message.outdatedClient ? "client" : "server"));
			
			String text = message.outdatedClient
					? "You have an outdated version of Iodine installed. "
					+ "Features will not work, you must update."
					: "The server has an outdated version of Iodine installed. "
					+ "Features will not work, ask the owner to update.";
			
			text = TextFormatting.GRAY + "[" + TextFormatting.YELLOW + "Iodine"
					+ TextFormatting.GRAY + "] " + TextFormatting.RED + text;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(text));
			return null;
		}
	}
}
