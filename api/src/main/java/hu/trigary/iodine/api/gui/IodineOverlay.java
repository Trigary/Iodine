package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an overlay that can be opened
 * for players who are in the {@link IodinePlayer.State#MODDED} state.
 */
public interface IodineOverlay extends IodineRoot<IodineOverlay>, DrawPrioritizeable<IodineOverlay> {
	/**
	 * Gets the position the coordinates are relative to.
	 *
	 * @return this overlay's anchor position
	 */
	@NotNull
	@Contract(pure = true)
	Anchor getAnchor();
	
	/**
	 * Gets the horizontal position offset of this overlay.
	 *
	 * @return this overlay's horizontal offset
	 */
	@Contract(pure = true)
	int getHorizontalOffset();
	
	/**
	 * Gets the vertical position offset of this overlay.
	 *
	 * @return this overlay's vertical offset
	 */
	@Contract(pure = true)
	int getVerticalOffset();
	
	
	
	/**
	 * Represents positions to which overlays can be anchored.
	 * This determines to what the coordinates should be relative to.
	 */
	enum Anchor {
		BOTTOM_LEFT(1),
		BOTTOM_CENTER(2),
		BOTTOM_RIGHT(3),
		LEFT_CENTER(4),
		CENTER(5),
		RIGHT_CENTER(6),
		TOP_LEFT(7),
		TOP_CENTER(8),
		TOP_RIGHT(9);
		
		private final int number;
		
		Anchor(int number) {
			this.number = number;
		}
		
		/**
		 * Gets the numpad number whose position represents this anchor.
		 * Can be used to extract row/column data.
		 *
		 * @return the numpad number representing the anchor
		 */
		@Contract(pure = true)
		public int getNumber() {
			return number;
		}
	}
}
