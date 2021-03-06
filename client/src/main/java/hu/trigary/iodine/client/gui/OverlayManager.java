package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The manager whose responsibility is managing the open {@link IodineOverlay}s.
 */
public abstract class OverlayManager {
	private final Map<Integer, IodineOverlay> openOverlays = new HashMap<>();
	private final Collection<IodineOverlay> drawOrderedOverlays = new TreeSet<>(Comparator
			.comparing(IodineOverlay::getDrawPriority)
			.thenComparing(IodineOverlay::getId));
	private final IodineMod mod;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
	protected OverlayManager(@NotNull IodineMod mod) {
		this.mod = mod;
	}
	
	
	
	/**
	 * Called when a packet was received instructing the client to open an overlay.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public final void packetOpenOverlay(@NotNull InputBuffer buffer) {
		//noinspection IOResourceOpenedButNotSafelyClosed
		IodineOverlay overlay = new IodineOverlay(mod, buffer.readInt(),
				buffer.readByte(), buffer.readShort(), buffer.readShort());
		mod.getLogger().debug("OverlayManager > opening {}", overlay.getId());
		openOverlays.put(overlay.getId(), overlay);
		drawOrderedOverlays.add(overlay);
		overlay.deserialize(buffer, false);
	}
	
	/**
	 * Called when a packet was received instructing the client to update an overlay.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public final void packetUpdateOverlay(@NotNull InputBuffer buffer) {
		IodineOverlay overlay = openOverlays.get(buffer.readInt());
		mod.getLogger().debug("OverlayManager > updating {}", overlay.getId());
		drawOrderedOverlays.remove(overlay);
		overlay.deserialize(buffer, true);
		drawOrderedOverlays.add(overlay);
	}
	
	/**
	 * Called when a packet was received instructing the client to close an overlay.
	 *
	 * @param buffer the buffer containing the packet
	 */
	public final void packetCloseOverlay(@NotNull InputBuffer buffer) {
		try (IodineOverlay overlay = openOverlays.remove(buffer.readInt())) {
			drawOrderedOverlays.remove(overlay);
			mod.getLogger().debug("OverlayManager > closing {}", overlay.getId());
		}
	}
	
	
	
	/**
	 * Should be called when the client resolution changes.
	 * Calls {@link IodineOverlay#update()} on all open overlays.
	 */
	public final void updateOverlayResolutions() {
		mod.getLogger().debug("OverlayManager > updating resolutions");
		for (IodineOverlay overlay : openOverlays.values()) {
			overlay.update();
		}
	}
	
	/**
	 * Should be called during the overlay rendering phase.
	 * Renders all open overlays on the screen.
	 *
	 * @param partialTicks the client's partial ticks, used for animations
	 */
	public final void drawOverlays(float partialTicks) {
		for (IodineOverlay overlay : drawOrderedOverlays) {
			overlay.draw(Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
		}
	}
	
	/**
	 * Closes all open overlays.
	 */
	public final void closeOverlays() {
		mod.getLogger().debug("OverlayManager > closing all");
		for (IodineOverlay overlay : openOverlays.values()) {
			overlay.close();
		}
		openOverlays.clear();
		drawOrderedOverlays.clear();
	}
}
