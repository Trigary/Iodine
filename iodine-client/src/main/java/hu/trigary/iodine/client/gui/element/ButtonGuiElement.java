package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

public abstract class ButtonGuiElement extends GuiElement {
	protected ButtonGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
}
