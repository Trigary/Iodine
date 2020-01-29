package hu.trigary.iodine.api.gui.element.base;

import hu.trigary.iodine.api.gui.AttachmentHolder;
import hu.trigary.iodine.api.gui.DrawPrioritizeable;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.gui.container.base.GuiParent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The base class for GUI elements.
 * All other elements extend this class.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiElement<T extends GuiElement<T>> extends AttachmentHolder<T>, DrawPrioritizeable<T> {
	/**
	 * Gets the GUI or overlay that contains this element.
	 *
	 * @return the instance that contains this element
	 */
	@NotNull
	@Contract(pure = true)
	IodineRoot<?> getRoot();
	
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
	 * A value of -1 is allowed as a way to indicate to leave that specific padding value unchanged.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setPadding(@NotNull int[] padding);
	
	/**
	 * Sets a single padding value.
	 * Same as calling {@link #setPadding(int[])} with -1
	 * as all array element values except this one.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default T setPaddingTop(int padding) {
		return setPadding(new int[]{padding, -1, -1, -1});
	}
	
	/**
	 * Sets a single padding value.
	 * Same as calling {@link #setPadding(int[])} with -1
	 * as all array element values except this one.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default T setPaddingBottom(int padding) {
		return setPadding(new int[]{-1, padding, -1, -1});
	}
	
	/**
	 * Sets a single padding value.
	 * Same as calling {@link #setPadding(int[])} with -1
	 * as all array element values except this one.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default T setPaddingLeft(int padding) {
		return setPadding(new int[]{-1, -1, padding, -1});
	}
	
	/**
	 * Sets a single padding value.
	 * Same as calling {@link #setPadding(int[])} with -1
	 * as all array element values except this one.
	 *
	 * @param padding the new padding value
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default T setPaddingRight(int padding) {
		return setPadding(new int[]{-1, -1, -1, padding});
	}
}
