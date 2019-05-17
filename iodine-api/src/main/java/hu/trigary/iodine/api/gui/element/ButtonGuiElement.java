package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiClickable;
import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiTextable;

/**
 * A GUI element that displays text and can be clicked.
 */
public interface ButtonGuiElement extends GuiElement<ButtonGuiElement>, GuiTextable<ButtonGuiElement>,
		GuiClickable<ButtonGuiElement>, GuiEditable<ButtonGuiElement> {
}
