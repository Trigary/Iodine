package hu.trigary.iodine.api.gui.element.base;

import hu.trigary.iodine.api.gui.AttachmentHolder;
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
public interface GuiElement<T extends GuiElement<T>> extends AttachmentHolder {
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
	 * Gets this element's draw priority.
	 *
	 * @return the current draw priority
	 * @see #setDrawPriority(short)
	 */
	@Contract(pure = true)
	short getDrawPriority();
	
	/**
	 * Sets the draw priority of this element.
	 * <br><br>
	 * This number determines whether this element should
	 * be rendered behind or in front of other elements.
	 * The element with the highest value is drawn on top of all other elements.
	 * <br><br>
	 * By default elements are drawn in the order they are added to the {@link IodineGui},
	 * so the default draw priority is an internal incrementing counter.
	 * <br><br>
	 * Changing the draw priority of a {@link hu.trigary.iodine.api.gui.container.base.GuiContainer}
	 * causes all of its children to update their priority:
	 * the child's draw priority is set to the container's draw priority
	 * if its previous value is less than this new value.
	 */
	void setDrawPriority(short priority);
}
