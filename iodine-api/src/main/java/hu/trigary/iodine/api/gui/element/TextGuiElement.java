package hu.trigary.iodine.api.gui.element;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays text.
 */
public interface TextGuiElement extends GuiElement {
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
	TextGuiElement setText(@NotNull String text);
}
