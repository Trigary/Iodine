package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that contains a continuous, non-discrete bar.
 * Elements have a minimum progress of 0 and a maximum progress of 1 (both inclusive).
 * The progress is 0 by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiProgressable<T extends GuiProgressable<T>> {
	/**
	 * Gets the current progress of this element.
	 *
	 * @return the current progress
	 */
	@Contract(pure = true)
	float getProgress();
	
	/**
	 * Sets the progress of this element.
	 * Must be at least 0 and at most 1.
	 *
	 * @param progress the new progress
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setProgress(float progress);
}
