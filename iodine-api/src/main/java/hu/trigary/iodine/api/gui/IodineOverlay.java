package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.gui.container.base.GuiBase;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an overlay that can be opened
 * for players who are in the {@link IodinePlayer.State#MODDED} state.
 */
public interface IodineOverlay extends GuiBase<IodineOverlay> {
	/**
	 * Gets the position the coordinates are relative to.
	 *
	 * @return this overlay's anchor position
	 */
	@NotNull
	@Contract(pure = true)
	Anchor getAnchor();
	
	
	
	/**
	 * Represents positions to which overlays can be anchored.
	 * This determines to what the coordinates should be relative to.
	 */
	enum Anchor {
		BOTTOM_LEFT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT,
		LEFT_CENTER,
		CENTER,
		RIGHT_CENTER,
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT
	}
}
