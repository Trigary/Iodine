package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that contains a non-solid bar.
 * Elements have a progress of 0 and a max progress of 100 by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiProgressable<T extends GuiProgressable<T>> {
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
	 * Sets the maximum progress of this element.
	 * Must be at least 0.
	 * If {@link #getProgress()} is higher than the new maximum progress,
	 * then it's reduced to the new maximum progress.
	 *
	 * @param maxProgress the new maximum progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setMaxProgress(int maxProgress);
	
	/**
	 * Sets the progress of this element.
	 * Must be at least 0 and at most {@link #getMaxProgress()}.
	 *
	 * @param progress the new progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setProgress(int progress);
}
