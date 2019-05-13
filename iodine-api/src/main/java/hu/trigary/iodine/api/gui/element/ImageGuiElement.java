package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiClickable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays an image and can be clicked.
 */
public interface ImageGuiElement extends GuiElement<ImageGuiElement>, GuiClickable<ImageGuiElement> {
	@NotNull
	@Contract(pure = true)
	byte[] getImage();
	
	@NotNull
	ImageGuiElement setImage(@NotNull byte[] image);
}
