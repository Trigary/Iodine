package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ContinuousSliderGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiUtils;
import net.minecraft.client.gui.widget.AbstractSlider;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ContinuousSliderGuiElement}.
 */
public class ContinuousGuiElementImpl extends ContinuousSliderGuiElement {
	private Slider widget;

	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public ContinuousGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new Slider(positionX, positionY, width, height, progress);
		widget.active = editable;
		widget.setMessage(text);
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
		if (widget.isHovered()) {
			IodineGuiUtils.renderTooltip(mouseX, mouseY, tooltip);
		}
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
		Slider(int x, int y, int width, int height, float progress) {
			super(x, y, width, height, progress);
		}
		
		float getProgress() {
			return (float) value;
		}
		
		@Override
		protected void updateMessage() {}
		
		@Override
		protected void applyValue() {}
	}
}
