package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ContinuousSliderGuiElement;
import net.minecraft.client.gui.widget.AbstractSlider;
import org.jetbrains.annotations.NotNull;

public class ContinuousGuiElementImpl extends ContinuousSliderGuiElement {
	private Slider widget;
	
	public ContinuousGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new Slider(positionX, positionY, width, height, progress);
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
		onChanged(widget.getProgress());
	}
	
	
	
	private static class Slider extends AbstractSlider {
		protected Slider(int x, int y, int width, int height, float progress) {
			super(x, y, width, height, progress);
		}
		
		public float getProgress() {
			return (float) value;
		}
		
		@Override
		protected void updateMessage() {}
		
		@Override
		protected void applyValue() {}
	}
}
