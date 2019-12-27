package hu.trigary.iodine.api.gui;

import org.jetbrains.annotations.Contract;

/**
 * Represents something that can be drawn in an environment where overlapping might happen.
 * <br><br>
 * The draw priority number determines whether this instance should
 * be rendered behind or in front of other instances.
 * The instance with the highest value is drawn on top of all other instances.
 * The number must be at least {@link #PRIORITY_LOWER_BOUND} and at most {@link #PRIORITY_UPPER_BOUND}.
 * <br><br>
 * By default elements are drawn in the order they are added,
 * so the default draw priority is an internal incrementing counter.
 * <br><br>
 * Changing the draw priority of a {@link hu.trigary.iodine.api.gui.container.base.GuiContainer}
 * causes all of its child {@link hu.trigary.iodine.api.gui.element.base.GuiElement}s to update their priority:
 * the child's draw priority is set to the container's draw priority
 * if its previous value is less than this new value.
 */
public interface DrawPrioritizeable {
	/**
	 * The inclusive lower bound for the draw priorities.
	 */
	int PRIORITY_LOWER_BOUND = Byte.MIN_VALUE;
	
	/**
	 * The inclusive upper bound for the draw priorities.
	 */
	int PRIORITY_UPPER_BOUND = Byte.MAX_VALUE;
	
	/**
	 * Gets this instance's draw priority.
	 * See the declaring class for more information.
	 *
	 * @return the current draw priority
	 */
	@Contract(pure = true)
	int getDrawPriority();
	
	/**
	 * Sets the draw priority of this element.
	 * See the declaring class for more information.
	 *
	 * @param priority the new draw priority
	 */
	void setDrawPriority(int priority);
}
