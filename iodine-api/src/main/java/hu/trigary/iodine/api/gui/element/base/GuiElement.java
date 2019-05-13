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
}
