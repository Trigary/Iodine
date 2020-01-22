package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;

/**
 * A GUI element that can be checked and unchecked.
 */
public interface CheckboxGuiElement extends GuiElement<CheckboxGuiElement>,
		GuiEditable<CheckboxGuiElement>, GuiTooltipable<CheckboxGuiElement>,
		GuiClickable<CheckboxGuiElement>, GuiCheckable<CheckboxGuiElement> {
}
