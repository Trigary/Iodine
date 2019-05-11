package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.IodineGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for GUI elements.
 * All other elements extend this class.
 */
public interface GuiElement {
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
}
