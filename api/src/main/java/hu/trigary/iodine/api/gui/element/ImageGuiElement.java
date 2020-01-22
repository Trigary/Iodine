package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays an image and can be clicked.
 */
public interface ImageGuiElement extends GuiElement<ImageGuiElement>,
		GuiWidthSettable<ImageGuiElement>, GuiHeightSettable<ImageGuiElement>,
		GuiTooltipable<ImageGuiElement>, GuiClickable<ImageGuiElement> {
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
