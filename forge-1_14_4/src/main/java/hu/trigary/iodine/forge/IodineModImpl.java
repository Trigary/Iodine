package hu.trigary.iodine.forge;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.forge.gui.GuiElementManagerImpl;
import hu.trigary.iodine.forge.gui.GuiManagerImpl;
import hu.trigary.iodine.forge.gui.OverlayManagerImpl;
import hu.trigary.iodine.forge.network.NetworkManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Mod("iodine")
public class IodineModImpl extends IodineMod {
	public IodineModImpl() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::joined);
		MinecraftForge.EVENT_BUS.addListener(this::quit);
	}
	
	
	
	private void setup(@NotNull FMLCommonSetupEvent event) {
		initializeFirst(LogManager.getLogger("Iodine"),
				ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().getQualifier());
		initializeSecond(new NetworkManagerImpl(this),
				new GuiElementManagerImpl(this),
				new GuiManagerImpl(this),
				new OverlayManagerImpl(this));
		getLogger().info("Iodine setup done");
	}
	
	private void joined(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
		//noinspection resource
		Minecraft.getInstance().enqueue(this::onJoinedServer);
	}
	
	private void quit(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
		//noinspection resource
		Minecraft.getInstance().enqueue(this::onQuitServer);
	}
	
	
	
	@Contract(pure = true)
	@Override
	public int getScreenWidth() {
		//noinspection resource
		return Minecraft.getInstance().mainWindow.getScaledWidth();
	}
	
	@Contract(pure = true)
	@Override
	public int getScreenHeight() {
		//noinspection resource
		return Minecraft.getInstance().mainWindow.getScaledHeight();
	}
}
