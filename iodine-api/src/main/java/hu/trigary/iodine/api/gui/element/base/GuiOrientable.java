package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can be oriented both vertically and horizontally.
 * Elements are in the horizontal orientation by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiOrientable<T extends GuiOrientable<T>> {
	/**
	 * Gets whether this element is in vertical orientation.
	 *
	 * @return true if this element is vertically oriented
	 */
	@Contract(pure = true)
	boolean isVerticalOrientation();
	
	/**
	 * Gets whether this element is in horizontal orientation.
	 *
	 * @return true if this element is horizontally oriented
	 */
	@Contract(pure = true)
	default boolean isHorizontalOrientation() {
		return !isVerticalOrientation();
	}
	
	/**
	 * Sets this element's orientation.
	 *
	 * @param vertical whether the element should be vertically oriented
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setOrientation(boolean vertical);
}
