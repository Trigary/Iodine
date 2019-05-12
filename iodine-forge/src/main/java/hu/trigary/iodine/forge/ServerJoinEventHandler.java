package hu.trigary.iodine.forge;

import hu.trigary.iodine.forge.network.out.ClientLoginPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.jetbrains.annotations.NotNull;

public class ServerJoinEventHandler {
	private final IodineMod mod;
	
	public ServerJoinEventHandler(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	@SubscribeEvent
	public void connected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		//packets can't yet be sent here
		Minecraft.getMinecraft().addScheduledTask(() -> MinecraftForge.EVENT_BUS.register(new InnerHandler()));
	}
	
	private class InnerHandler {
		@SubscribeEvent
		public void joined(EntityJoinWorldEvent event) {
			mod.getLogger().info("Joined server, attempting login");
			mod.getNetwork().send(new ClientLoginPacket(mod.getVersion()));
			MinecraftForge.EVENT_BUS.unregister(this); //don't let this listener fire multiple times
		}
	}
}
