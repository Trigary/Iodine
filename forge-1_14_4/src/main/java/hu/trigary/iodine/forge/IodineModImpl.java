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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

@Mod("iodine")
public class IodineModImpl extends IodineMod {
	public IodineModImpl() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::connected);
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
	
	private void connected(@NotNull ClientPlayerNetworkEvent.LoggedInEvent event) {
		//noinspection resource
		Minecraft.getInstance().enqueue(this::onJoinedServer);
	}
	
	
	
	@NotNull
	@Override
	public IntPair getScreenSize() {
		//noinspection resource
		MainWindow window = Minecraft.getInstance().mainWindow;
		return new IntPair(window.getScaledWidth(), window.getScaledHeight());
	}
}
