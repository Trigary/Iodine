package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can display a tooltip when hovered over.
 * The tooltips are empty {@link String}s by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiTooltipable<T extends GuiTooltipable<T>> {
	/**
	 * Gets the current tooltip.
	 *
	 * @return the current tooltip
	 */
	@NotNull
	@Contract(pure = true)
	String getTooltip();
	
	/**
	 * Sets the tooltip.
	 *
	 * @param tooltip the new tooltip
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setTooltip(@NotNull String tooltip);
}
