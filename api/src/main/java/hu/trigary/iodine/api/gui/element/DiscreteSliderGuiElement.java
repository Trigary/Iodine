package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a discrete, non-continuous slider, with text on top of it.
 * By default progress, min-progress and max-progress are 0, 0 and 100, respectively.
 * Only the width or the height is customizable (not both) depending on the orientation.
 * The non-configurable dimension's getter returns 0.
 */
public interface DiscreteSliderGuiElement extends GuiElement<DiscreteSliderGuiElement>, GuiOrientable<DiscreteSliderGuiElement>,
		GuiWidthSettable<DiscreteSliderGuiElement>, GuiHeightSettable<DiscreteSliderGuiElement>,
		GuiTextable<DiscreteSliderGuiElement>, GuiEditable<DiscreteSliderGuiElement> {
	/**
	 * Gets the current minimum progress of this element.
	 *
	 * @return the current minimum progress
	 */
	@Contract(pure = true)
	int getMinProgress();
	
	/**
	 * Gets the current maximum progress of this element.
	 *
	 * @return the current maximum progress
	 */
	@Contract(pure = true)
	int getMaxProgress();
	
	/**
	 * Gets the current progress of this element.
	 *
	 * @return the current progress
	 */
	@Contract(pure = true)
	int getProgress();
	
	
	
	/**
	 * Sets the minimum progress of this element.
	 * Must be at most {@link #getMaxProgress()}.
	 * If {@link #getProgress()} is lower than this new minimum progress,
	 * then it is set to the new minimum progress.
	 *
	 * @param minProgress the new minimum progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DiscreteSliderGuiElement setMinProgress(int minProgress);
	
	/**
	 * Sets the maximum progress of this element.
	 * Must be at least {@link #getMinProgress()}.
	 * If {@link #getProgress()} is higher than this new maximum progress,
	 * then it is set to the new maximum progress.
	 *
	 * @param maxProgress the new maximum progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DiscreteSliderGuiElement setMaxProgress(int maxProgress);
	
	/**
	 * Sets the progress of this element.
	 * Must be at least {@link #getMinProgress()} and at most {@link #getMaxProgress()}.
	 *
	 * @param progress the new progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DiscreteSliderGuiElement setProgress(int progress);
	
	
	
	/**
	 * Sets the action that should be executed when
	 * this GUI element's progress is changed by a player.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DiscreteSliderGuiElement onProgressed(@Nullable ProgressedAction action);
	
	
	
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
		void accept(@NotNull DiscreteSliderGuiElement element, int oldProgress, int newProgress, @NotNull IodinePlayer player);
	}
}
