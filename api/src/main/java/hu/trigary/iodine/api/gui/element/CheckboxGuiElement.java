package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiCheckable;
import hu.trigary.iodine.api.gui.element.base.GuiClickable;
import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;

/**
 * A GUI element that can be checked and unchecked.
 */
public interface CheckboxGuiElement extends GuiElement<CheckboxGuiElement>, GuiCheckable<CheckboxGuiElement>,
		GuiClickable<CheckboxGuiElement>, GuiEditable<CheckboxGuiElement> {
}
