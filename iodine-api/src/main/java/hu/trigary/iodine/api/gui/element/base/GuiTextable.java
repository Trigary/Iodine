package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can display text.
 * The text of the elements are empty {@link String}s by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiTextable<T extends GuiTextable<T>> {
	/**
	 * Gets the text that is currently displayed.
	 *
	 * @return the currently displayed text
	 */
	@NotNull
	@Contract(pure = true)
	String getText();
	
	/**
	 * Sets the text that should be displayed.
	 *
	 * @param text the text to display
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setText(@NotNull String text);
}
