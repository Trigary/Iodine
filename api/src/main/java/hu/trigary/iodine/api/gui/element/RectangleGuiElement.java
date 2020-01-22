package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import hu.trigary.iodine.api.gui.container.GridGuiContainer;

/**
 * A GUI element that is a colored rectangle.
 * Useful in combination with the {@link GridGuiContainer} when creating dynamic images, minimaps.
 */
public interface RectangleGuiElement extends GuiElement<RectangleGuiElement>,
		GuiWidthSettable<RectangleGuiElement>, GuiHeightSettable<RectangleGuiElement>,
		GuiTooltipable<RectangleGuiElement>, GuiClickable<RectangleGuiElement>,
		GuiColorable<RectangleGuiElement> {
}
