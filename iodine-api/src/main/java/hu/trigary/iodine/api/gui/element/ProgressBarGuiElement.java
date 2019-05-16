package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiOrientable;
import hu.trigary.iodine.api.gui.element.base.GuiProgressable;
import hu.trigary.iodine.api.gui.element.base.GuiTextable;

/**
 * A GUI element that is a progress bar, with text on top of it.
 */
public interface ProgressBarGuiElement extends GuiElement<ProgressBarGuiElement>, GuiTextable<ProgressBarGuiElement>,
		GuiProgressable<ProgressBarGuiElement>, GuiOrientable<ProgressBarGuiElement> {
}
