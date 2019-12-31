package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.RectangleGuiElement;
import net.minecraft.client.gui.AbstractGui;
import org.jetbrains.annotations.NotNull;

public class RectangleGuiElementImpl extends RectangleGuiElement {
	public RectangleGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		AbstractGui.fill(positionX, positionY, width, height, color);
	}
}
