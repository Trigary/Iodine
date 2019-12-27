package hu.trigary.iodine.client.gui.container.base;

import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

public abstract class GuiContainer extends GuiElement implements GuiParent {
	
	protected GuiContainer(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	public abstract void initialize();
	
	@NotNull
	protected final GuiElement[] resolveChildren(@NotNull int[] childrenIdentifiers) {
		GuiElement[] children = new GuiElement[childrenIdentifiers.length];
		for (int i = 0; i < children.length; i++) {
			children[i] = getGui().getElement(childrenIdentifiers[i]);
		}
		return children;
	}
	
	@Override
	protected abstract void setChildrenPositions(int offsetX, int offsetY);
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {}
	
	@Override
	protected void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks) {}
}
