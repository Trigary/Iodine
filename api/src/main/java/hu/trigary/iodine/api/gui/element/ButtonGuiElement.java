package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;

/**
 * A GUI element that displays text and can be clicked.
 */
public interface ButtonGuiElement extends GuiElement<ButtonGuiElement>,
		GuiWidthSettable<ButtonGuiElement>, GuiTextable<ButtonGuiElement>,
		GuiClickable<ButtonGuiElement>, GuiEditable<ButtonGuiElement> {
}
