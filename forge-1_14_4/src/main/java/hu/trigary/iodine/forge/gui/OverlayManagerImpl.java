package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.OverlayManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

public class OverlayManagerImpl extends OverlayManager {
	public OverlayManagerImpl(@NotNull IodineMod mod) {
		super(mod);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::render);
	}
	
	
	
	private void render(@NotNull RenderGameOverlayEvent event) {
		drawOverlays(event.getPartialTicks());
	}
}
