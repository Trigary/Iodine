package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.SliderGuiElement;
import net.minecraft.client.gui.widget.AbstractSlider;
import org.jetbrains.annotations.NotNull;

public class SliderGuiElementImpl extends SliderGuiElement {
	private AbstractSlider widget;
	
	public SliderGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new AbstractSlider(positionX, positionY, width, height, (double) progress / maxProgress) {
			@Override
			protected void updateMessage() {}
			
			@Override
			protected void applyValue() {
				onChanged((int) (value * maxProgress));
			}
		};
		widget.active = editable;
		widget.setMessage(text);
		//TODO verticalOrientation
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		return widget.mouseClicked(mouseX, mouseY, 0);
	}
	
	@Override
	public void onMouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
		widget.mouseClicked(mouseX, mouseY, 0);
	}
	
	@Override
	public void onMouseReleased(double mouseX, double mouseY) {
		widget.mouseReleased(mouseX, mouseY, 0);
	}
	
	//TODO most elements don't update values when the client changes them, but instead wait for the server
}
