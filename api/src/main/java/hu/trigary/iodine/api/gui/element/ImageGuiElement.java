package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

/**
 * A GUI element that displays an image and can be clicked.
 * The image mustn't be bigger than 1 MB (2^20 bytes).
 */
public interface ImageGuiElement extends GuiElement<ImageGuiElement>,
		GuiWidthSettable<ImageGuiElement>, GuiHeightSettable<ImageGuiElement>,
		GuiTooltipable<ImageGuiElement>, GuiClickable<ImageGuiElement>,
		GuiResizable<ImageGuiElement> {
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
	 * Mutating the {@code byte[]} parameter causes undefined behaviour.
	 *
	 * @param image the new image
	 * @return the current instance (for chaining)
	 */
	@NotNull
	ImageGuiElement setImage(@NotNull byte[] image);
}
