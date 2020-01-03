package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.RectangleGuiElement;
import net.minecraft.client.gui.AbstractGui;
import org.jetbrains.annotations.NotNull;

public class RectangleGuiElementImpl extends RectangleGuiElement {
	private int positionX;
	private int positionY;
	
	public RectangleGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		AbstractGui.fill(positionX, positionY, width, height, color);
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		if (mouseX >= positionX && mouseY >= positionY && mouseX < positionX + width && mouseY < positionY + height) {
			onChanged();
			return true;
		}
		return false;
	}
}
