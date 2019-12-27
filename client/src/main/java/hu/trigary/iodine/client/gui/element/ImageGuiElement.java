package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

public abstract class ImageGuiElement extends GuiElement {
	protected ImageGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
}