package hu.trigary.iodine.forge;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.forge.gui.GuiElementManagerImpl;
import hu.trigary.iodine.forge.gui.GuiManagerImpl;
import hu.trigary.iodine.forge.gui.OverlayManagerImpl;
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
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Mod("iodine")
public class IodineModBase extends IodineMod {
	public IodineModBase() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);
		eventBus.addListener(new ServerJoinListener()::connected);
	}
	
	
	
	private void setup(@NotNull FMLCommonSetupEvent event) {
		initialize(Logger.getLogger("Iodine"), //TODO is a java.util logger fine or do I need a slf4j logger?
				ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().getQualifier(),
				new NetworkManagerImpl(this),
				new GuiElementManagerImpl(this),
				new GuiManagerImpl(this),
				new OverlayManagerImpl(this));
	}
	
	
	
	@NotNull
	@Override
	public IntPair getScreenSize() {
		MainWindow window = Minecraft.getInstance().mainWindow;
		return new IntPair(window.getScaledWidth(), window.getScaledHeight());
		//TODO this is probably incorrect, compare it to other callbacks
	}
	
	
	
	private class ServerJoinListener {
		private void connected(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
			//TODO can packets be sent now, or do I have to wait for EntityJoinWorldEvent?
			//TODO is this fired async or sync? do I need to switch to the main thread?
			//noinspection resource
			Minecraft.getInstance().enqueue(() -> FMLJavaModLoadingContext.get().getModEventBus().register(this));
			//packets can't be sent here
		}
	
		@SubscribeEvent
		private void joined(@NotNull EntityJoinWorldEvent event) {
			FMLJavaModLoadingContext.get().getModEventBus().unregister(this);
			//don't let this fire multiple times
			onJoinedServer();
		}
	}
}
