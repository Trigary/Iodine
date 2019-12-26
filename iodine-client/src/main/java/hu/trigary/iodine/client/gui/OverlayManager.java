package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.IodineModBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class OverlayManager {
	private final Map<Integer, IodineOverlay> openOverlays = new HashMap<>();
	private final Collection<IodineOverlay> drawOrderedOverlays = new TreeSet<>(Comparator
			.comparing(IodineOverlay::getDrawPriority)
			.thenComparing(IodineOverlay::getId));
	private final IodineModBase mod;
	
	protected OverlayManager(@NotNull IodineModBase mod) {
		this.mod = mod;
	}
	
	
	
	public final void packetOpenOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = new IodineOverlay(mod, buffer.getInt(),
				buffer.get(), buffer.getShort(), buffer.getShort());
		openOverlays.put(overlay.getId(), overlay);
		drawOrderedOverlays.add(overlay);
		overlay.deserialize(buffer);
		mod.getLogger().info("Opened overlay: " + overlay.getId());
	}
	
	public final void packetUpdateOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.get(buffer.getInt());
		drawOrderedOverlays.remove(overlay);
		overlay.deserialize(buffer);
		drawOrderedOverlays.add(overlay);
		mod.getLogger().info("Updated overlay: " + overlay.getId());
	}
	
	public final void packetCloseOverlay(@NotNull ByteBuffer buffer) {
		IodineOverlay overlay = openOverlays.remove(buffer.getInt());
		mod.getLogger().info("Closed overlay: " + overlay.getId());
	}
	
	
	
	public final void drawOverlays(int mouseX, int mouseY, float partialTicks) {
		for (IodineOverlay overlay : drawOrderedOverlays) {
			overlay.draw(mouseX, mouseY, partialTicks);
		}
	}
}
