package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class OverlayManager {
	private final Map<Integer, IodineOverlay> openOverlays = new HashMap<>();
	private final Collection<IodineOverlay> drawOrderedOverlays = new TreeSet<>(Comparator
			.comparing(IodineOverlay::getDrawPriority)
			.thenComparing(IodineOverlay::getId));
	private final IodineMod mod;
	
	protected OverlayManager(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	public final void packetOpenOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = new IodineOverlay(mod, buffer.getInt(),
				buffer.get(), buffer.getShort(), buffer.getShort());
		mod.getLogger().debug("OverlayManager > opening {}", overlay.getId());
		openOverlays.put(overlay.getId(), overlay);
		drawOrderedOverlays.add(overlay);
		overlay.deserialize(buffer);
	}
	
	public final void packetUpdateOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.get(buffer.getInt());
		mod.getLogger().debug("OverlayManager > updating {}", overlay.getId());
		drawOrderedOverlays.remove(overlay);
		overlay.deserialize(buffer);
		drawOrderedOverlays.add(overlay);
	}
	
	public final void packetCloseOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.remove(buffer.getInt());
		mod.getLogger().debug("OverlayManager > closing {}", overlay.getId());
	}
	
	
	
	public final void drawOverlays(float partialTicks) {
		for (IodineOverlay overlay : drawOrderedOverlays) {
			overlay.draw(Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
		}
	}
}
