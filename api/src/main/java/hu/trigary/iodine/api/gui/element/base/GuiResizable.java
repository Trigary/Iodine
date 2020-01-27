package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} whose graphical content can be resized.
 * The default {@link ResizeMode} is {@link ResizeMode#STRETCH}.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiResizable<T extends GuiResizable<T>> {
	/**
	 * Gets the active resizing logic.
	 *
	 * @return the current resizing logic.
	 */
	@NotNull
	@Contract(pure = true)
	ResizeMode getResizeMode();
	
	/**
	 * Sets the resizing logic to use.
	 *
	 * @param resizeMode the new resize mode
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setResizeMode(@NotNull ResizeMode resizeMode);
	
	
	
	/**
	 * The logic using which the graphical content's size should be adjusted to match the element's size.
	 */
	enum ResizeMode { //TODO how to serialize?
		STRETCH,
		REPEAT,
		NONE
		
		//TODO define stuff depending on what I can implement client-side, but optimally:
		// magnification: original; repeat; linear interpolation; nearest neighbour interpolation
		// minification: cut (keep left and top); linear interpolation; nearest neighbour interpolation
	}
}
