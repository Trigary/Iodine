package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.CheckboxGuiElement;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link CheckboxGuiElement}.
 */
public class CheckboxGuiElementImpl extends CheckboxGuiElement {
	private CheckboxButton widget;

	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public CheckboxGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new CheckboxButton(positionX, positionY, width, height, "", checked) {
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
