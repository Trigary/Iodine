package hu.trigary.iodine.api.gui.element.base;

import hu.trigary.iodine.api.gui.IodineColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can be colored.
 * The default {@link IodineColor} is {@link IodineColor#WHITE}.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiColorable<T extends GuiColorable<T>> {
	/**
	 * Gets this element's current color.
	 *
	 * @return the current color
	 */
	@NotNull
	@Contract(pure = true)
	IodineColor getColor();
	
	/**
	 * Sets this element's color.
	 *
	 * @param color the new color
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setColor(@NotNull IodineColor color);
}
