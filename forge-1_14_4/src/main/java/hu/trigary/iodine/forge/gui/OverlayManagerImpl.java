package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.OverlayManager;
import hu.trigary.iodine.forge.IodineModImpl;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link OverlayManager}.
 */
public class OverlayManagerImpl extends OverlayManager {
	private final IodineModImpl mod;
	private int lastWidth;
	private int lastHeight;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
	public OverlayManagerImpl(@NotNull IodineModImpl mod) {
		super(mod);
		this.mod = mod;
		MinecraftForge.EVENT_BUS.addListener(this::render);
	}
	
	
	
	private void render(@NotNull RenderGameOverlayEvent event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		
		if (lastWidth != mod.getScreenWidth() || lastHeight != mod.getScreenHeight()) {
			lastWidth = mod.getScreenWidth();
			lastHeight = mod.getScreenHeight();
			updateOverlayResolutions();
		}
		
		drawOverlays(event.getPartialTicks());
	}
}
