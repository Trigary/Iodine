package hu.trigary.iodine.forge;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.forge.gui.ElementManagerImpl;
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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
		MinecraftForge.EVENT_BUS.addListener(this::onJoined);
		MinecraftForge.EVENT_BUS.addListener(this::onQuit);
	}
	
	
	
	private void onSetup(@NotNull FMLCommonSetupEvent event) {
		initialize(LogManager.getLogger("Iodine"),
				ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().getQualifier());
		setup(new NetworkManagerImpl(this),
				new ElementManagerImpl(),
				new GuiManagerImpl(this),
				new OverlayManagerImpl(this));
		getLogger().info("Iodine setup done");
	}
	
	private void onJoined(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
		//noinspection resource
		Minecraft.getInstance().enqueue(this::onJoinedServer);
	}
	
	private void onQuit(@NotNull ClientPlayerNetworkEvent.LoggedOutEvent event) {
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
