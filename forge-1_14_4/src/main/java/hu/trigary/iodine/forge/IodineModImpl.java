package hu.trigary.iodine.forge;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.forge.gui.GuiElementManagerImpl;
import hu.trigary.iodine.forge.gui.GuiManagerImpl;
import hu.trigary.iodine.forge.gui.OverlayManagerImpl;
import hu.trigary.iodine.forge.network.NetworkManagerImpl;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

@Mod("iodine")
public class IodineModImpl extends IodineMod {
	public IodineModImpl() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);
		eventBus.addListener(new ServerJoinListener()::connected);
	}
	
	
	
	private void setup(@NotNull FMLCommonSetupEvent event) {
		initialize(LogManager.getLogger("Iodine"),
				ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().getQualifier(),
				new NetworkManagerImpl(this),
				new GuiElementManagerImpl(this),
				new GuiManagerImpl(this),
				new OverlayManagerImpl(this));
		getLogger().info("Iodine setup done");
	}
	
	
	
	@NotNull
	@Override
	public IntPair getScreenSize() {
		//noinspection resource
		MainWindow window = Minecraft.getInstance().mainWindow;
		return new IntPair(window.getScaledWidth(), window.getScaledHeight());
	}
	
	
	
	private class ServerJoinListener {
		private void connected(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
			//noinspection resource
			Minecraft.getInstance().enqueue(() -> FMLJavaModLoadingContext.get().getModEventBus().register(this));
			//packets can't be sent here
		}
	
		@SubscribeEvent
		public void joined(@NotNull EntityJoinWorldEvent event) {
			FMLJavaModLoadingContext.get().getModEventBus().unregister(this);
			//don't let this fire multiple times
			onJoinedServer();
		}
	}
}
