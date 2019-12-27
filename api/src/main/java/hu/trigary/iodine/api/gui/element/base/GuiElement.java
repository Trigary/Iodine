package hu.trigary.iodine.api.gui.element.base;

import hu.trigary.iodine.api.gui.AttachmentHolder;
import hu.trigary.iodine.api.gui.DrawPrioritizeable;
import hu.trigary.iodine.api.gui.container.base.GuiBase;
import hu.trigary.iodine.api.gui.container.base.GuiParent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for GUI elements.
 * All other elements extend this class.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiElement<T extends GuiElement<T>> extends AttachmentHolder, DrawPrioritizeable {
	/**
	 * The inclusive upper bound for padding values.
	 */
	int PADDING_UPPER_BOUND = Short.MAX_VALUE;
	
	/**
	 * Gets the GUI that contains this element.
	 *
	 * @return the GUI that contains this element
	 */
	@NotNull
	@Contract(pure = true)
	GuiBase<?> getGui();
	
	/**
	 * Gets the ID of this element that was specified during creation.
	 *
	 * @return the ID of this element
	 */
	@NotNull
	@Contract(pure = true)
	Object getId();
	
	/**
	 * Gets the parent of this element.
	 *
	 * @return the parent of this element
	 */
	@NotNull
	@Contract(pure = true)
	GuiParent<?> getParent();
	
	/**
	 * Gets the current padding values.
	 * The elements in the array are the top, bottom, left, right padding values in this order.
	 *
	 * @return the current padding values
	 */
	@NotNull
	@Contract(pure = true)
	int[] getPadding();
	
	/**
	 * Sets the padding values.
	 * The elements in the array must be the top, bottom, left, right padding values in this order.
	 * The values must be at least 0 and at most {@link #PADDING_UPPER_BOUND}.
	 * {@code -1} is also allowed as a way to indicate to leave that specific padding value unchanged.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	GuiElement<T> setPadding(@NotNull int[] padding);
}
