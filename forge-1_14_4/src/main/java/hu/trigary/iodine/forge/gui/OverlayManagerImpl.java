package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.OverlayManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class OverlayManagerImpl extends OverlayManager {
	private int lastWidth;
	private int lastHeight;
	
	public OverlayManagerImpl(@NotNull IodineMod mod) {
		super(mod);
		MinecraftForge.EVENT_BUS.addListener(this::render);
	}
	
	
	
	private void render(@NotNull RenderGameOverlayEvent event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		
		if (lastWidth != getMod().getScreenWidth() || lastHeight != getMod().getScreenHeight()) {
			lastWidth = getMod().getScreenWidth();
			lastHeight = getMod().getScreenHeight();
			updateOverlayResolutions();
		}
		
		drawOverlays(event.getPartialTicks());
	}
}
