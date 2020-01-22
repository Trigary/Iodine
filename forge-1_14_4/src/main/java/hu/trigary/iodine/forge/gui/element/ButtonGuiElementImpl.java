package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ButtonGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends ButtonGuiElement {
	private AbstractButton widget;

	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public ButtonGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
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
		if (widget.isHovered()) {
			IodineGuiScreen.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		return widget.mouseClicked(mouseX, mouseY, 0);
	}
}
