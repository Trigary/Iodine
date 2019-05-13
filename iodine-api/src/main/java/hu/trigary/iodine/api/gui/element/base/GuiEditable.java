package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can be optionally editable by viewers.
 * Elements that do not implement this interface are not editable by the viewers.
 * Elements are editable by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiEditable<T extends GuiEditable<T>> {
	/**
	 * Gets whether viewers are able to edit this element.
	 *
	 * @return whether this element is editable
	 */
	@Contract(pure = true)
	boolean isEditable();
	
	/**
	 * Sets whether viewers should be able to edit this element.
	 *
	 * @param editable whether this element should be editable
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setEditable(boolean editable);
}
