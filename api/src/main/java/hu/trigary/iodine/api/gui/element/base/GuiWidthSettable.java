package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that has a modifiable width.
 * Different element types have different default widths.
 * The width value must be positive and at most {@link #WIDTH_UPPER_BOUND}.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiWidthSettable<T extends GuiWidthSettable<T>> {
	/**
	 * The inclusive upper bound for the width.
	 */
	int WIDTH_UPPER_BOUND = Short.MAX_VALUE;
	
	/**
	 * Gets the active width.
	 *
	 * @return the currently used width
	 */
	@Contract(pure = true)
	int getWidth();
	
	/**
	 * Sets the active width.
	 *
	 * @param width the width to use
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setWidth(int width);
}
