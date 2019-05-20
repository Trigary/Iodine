package hu.trigary.iodine.api.gui.element.base;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.container.base.GuiParent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for GUI elements.
 * All other elements extend this class.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiElement<T extends GuiElement<T>> {
	/**
	 * Gets the GUI that contains this element.
	 *
	 * @return the GUI that contains this element
	 */
	@NotNull
	@Contract(pure = true)
	IodineGui getGui();
	
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
	 * Gets the offset on the specified side.
	 * This side must not be a compound side:
	 * it can only be left, right, top or bottom.
	 *
	 * @param side the side to get the offset on
	 * @return the offset on the specified side
	 */
	@Contract(pure = true)
	int getOffset(int side);
	
	/**
	 * Sets the offset on the specified sides.
	 * The side can be a compound side.
	 * The amount must be at least 0 and at most {@link Short#MAX_VALUE}.
	 *
	 * @param sides the sides to set the offset on
	 * @param amount the amount of the offset
	 * @return the current instance (for chaining)
	 */
	@NotNull
	GuiElement<T> setOffset(int sides, int amount);
}
