package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that has a modifiable height.
 * Different element types have different default heights.
 * The height value must be positive and at most {@link Short#MAX_VALUE}.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiHeightSettable<T extends GuiHeightSettable<T>> {
	/**
	 * Gets the active height.
	 *
	 * @return the currently used height
	 */
	@Contract(pure = true)
	int getHeight();
	
	/**
	 * Sets the active height.
	 *
	 * @param height the height to use
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setHeight(int height);
}
