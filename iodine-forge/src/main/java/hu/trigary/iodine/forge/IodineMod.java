package hu.trigary.iodine.forge;

import hu.trigary.iodine.forge.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(modid = "iodine", useMetadata = true, clientSideOnly = true)
public class IodineMod {
	private Logger logger;
	private String version;
	private NetworkManager network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		version = event.getModMetadata().version;
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		network = new NetworkManager(this);
		MinecraftForge.EVENT_BUS.register(new ServerJoinEventHandler(this));
	}
	
	
	
	@NotNull
	public Logger getLogger() {
		return logger;
	}
	
	@NotNull
	public String getVersion() {
		return version;
	}
	
	@NotNull
	public NetworkManager getNetwork() {
		return network;
	}
}
