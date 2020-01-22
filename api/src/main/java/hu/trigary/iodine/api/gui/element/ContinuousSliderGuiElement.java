package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a continuous, non-discrete slider, with text on top of it.
 * Only the width or the height is customizable (not both) depending on the orientation.
 * The non-configurable dimension's getter returns 0.
 */
public interface ContinuousSliderGuiElement extends GuiElement<ContinuousSliderGuiElement>,
		GuiOrientable<ContinuousSliderGuiElement>, GuiWidthSettable<ContinuousSliderGuiElement>,
		GuiHeightSettable<ContinuousSliderGuiElement>, GuiEditable<ContinuousSliderGuiElement>,
		GuiTooltipable<ContinuousSliderGuiElement>, GuiTextable<ContinuousSliderGuiElement>,
		GuiProgressable<ContinuousSliderGuiElement> {
	/**
	 * Sets the action that should be executed when
	 * this GUI element's progress is changed by a player.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	ContinuousSliderGuiElement onProgressed(@Nullable ProgressedAction action);
	
	
	
	/**
	 * The handler of the progress changed action.
	 */
	@FunctionalInterface
	interface ProgressedAction {
		/**
		 * Handles the progress changed action.
		 *
		 * @param element the element that progressed
		 * @param oldProgress the progress before this change
		 * @param newProgress the progress that was set by the player
		 * @param player the player who changed the progress
		 */
		void accept(@NotNull ContinuousSliderGuiElement element, float oldProgress, float newProgress, @NotNull IodinePlayer player);
	}
}
