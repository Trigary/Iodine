package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.RadioButtonGuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class RadioButtonGuiElementImpl extends RadioButtonGuiElement {
	public RadioButtonGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}



	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		throw new NotImplementedException("");
	}

	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {

	}
}
