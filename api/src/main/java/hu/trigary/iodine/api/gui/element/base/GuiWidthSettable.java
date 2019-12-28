package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that has a modifiable width.
 * Different element types have different default widths.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiWidthSettable<T extends GuiWidthSettable<T>> {
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
