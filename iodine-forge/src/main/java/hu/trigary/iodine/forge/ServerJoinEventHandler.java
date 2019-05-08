package hu.trigary.iodine.forge;

import hu.trigary.iodine.forge.network.packet.LoginPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerJoinEventHandler {
	@SubscribeEvent
	public void connected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		Minecraft.getMinecraft().addScheduledTask(() -> MinecraftForge.EVENT_BUS.register(new InnerHandler()));
	}
	
	private static class InnerHandler {
		@SubscribeEvent
		public void joined(EntityJoinWorldEvent event) {
			IodineMod mod = IodineMod.getInstance();
			mod.getLogger().info("Joined server, attempting login");
			mod.getNetwork().send(new LoginPacket(mod.getVersion()));
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
