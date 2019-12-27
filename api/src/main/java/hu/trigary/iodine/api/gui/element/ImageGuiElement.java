package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiClickable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiHeightSettable;
import hu.trigary.iodine.api.gui.element.base.GuiWidthSettable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays an image and can be clicked.
 */
public interface ImageGuiElement extends GuiElement<ImageGuiElement>, GuiClickable<ImageGuiElement>,
		GuiWidthSettable<ImageGuiElement>, GuiHeightSettable<ImageGuiElement> {
	/**
	 * Gets the active image.
	 * Mutating the returned {@code byte[]} causes undefined behaviour.
	 *
	 * @return the current image
	 */
	@NotNull
	@Contract(pure = true)
	byte[] getImage();
	
	
	
	/**
	 * Sets the active image.
	 * Mutating the {@code byte[]} which was passed as parameter causes undefined behaviour.
	 *
	 * @param image the new image
	 * @return the current instance (for chaining)
	 */
	@NotNull
	ImageGuiElement setImage(@NotNull byte[] image);
}
