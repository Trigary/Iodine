package hu.trigary.iodine.client.gui.container.base;

import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

public abstract class GuiContainer extends GuiElement implements GuiParent {
	
	protected GuiContainer(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	public abstract void initialize();
	
	@Override
	public final void setPosition(int x, int y) {
		super.setPosition(x, y);
		setChildrenPosition(x, y);
	}
	
	protected abstract void setChildrenPosition(int thisX, int thisY);
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {}
	
	@Override
	protected void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks) {}
}
