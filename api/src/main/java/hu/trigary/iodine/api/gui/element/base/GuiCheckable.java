package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can be checked and unchecked.
 * Elements are unchecked by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiCheckable<T extends GuiCheckable<T>> {
	/**
	 * Gets whether this element is currently in its checked state.
	 *
	 * @return whether this element is checked
	 */
	@Contract(pure = true)
	boolean isChecked();
	
	/**
	 * Sets this element's checked state.
	 *
	 * @param checked the new checked state
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setChecked(boolean checked);
}
