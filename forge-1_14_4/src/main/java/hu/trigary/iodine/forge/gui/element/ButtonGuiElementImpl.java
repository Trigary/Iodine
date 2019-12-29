package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.ButtonGuiElement;
import net.minecraft.client.gui.widget.button.AbstractButton;
import org.jetbrains.annotations.NotNull;

public class ButtonGuiElementImpl extends ButtonGuiElement {
	private AbstractButton widget;
	
	public ButtonGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new AbstractButton(positionX, positionY, width, height, text) {
			@Override
			public void onPress() {
				onChanged();
			}
		};
		widget.active = editable;
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		return widget.mouseClicked(mouseX, mouseY, 0);
	}
}
