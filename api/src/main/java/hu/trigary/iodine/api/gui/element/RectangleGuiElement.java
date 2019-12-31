package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiColorable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiHeightSettable;
import hu.trigary.iodine.api.gui.element.base.GuiWidthSettable;
import hu.trigary.iodine.api.gui.container.GridGuiContainer;

/**
 * A GUI element that is a colored rectangle.
 * Useful in combination with the {@link GridGuiContainer} when creating dynamic images, minimaps.
 */
public interface RectangleGuiElement extends GuiElement<RectangleGuiElement>, GuiColorable<RectangleGuiElement>,
		GuiWidthSettable<RectangleGuiElement>, GuiHeightSettable<RectangleGuiElement> {
}
