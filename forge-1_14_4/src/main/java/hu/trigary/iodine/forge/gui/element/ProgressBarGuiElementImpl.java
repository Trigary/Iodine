package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.ProgressBarGuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class ProgressBarGuiElementImpl extends ProgressBarGuiElement {
	public ProgressBarGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}



	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		throw new NotImplementedException("");
	}

	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		//TODO something similar to bossbar/experience bar with the color of the furnace smelting progress
	}
}
