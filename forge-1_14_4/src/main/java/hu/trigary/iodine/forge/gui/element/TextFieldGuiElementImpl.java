package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.TextFieldGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link TextFieldGuiElement}.
 */
public class TextFieldGuiElementImpl extends TextFieldGuiElement {
	private TextFieldWidget widget;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public TextFieldGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int positionX, int positionY, int width, int height) {
		widget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, positionX, positionY, width, height, text);
		widget.active = editable; //TODO doesn't work, maybe check editable in onTyped methods?
		widget.setText(text);
		widget.setMaxStringLength(maxLength);
		widget.setValidator(regex == null ? s -> true : s -> regex.matcher(s).matches());
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawTooltipImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY) {
		if (widget.isHovered()) {
			IodineGuiUtils.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public void setFocused(boolean focused) {
		widget.setFocused2(focused);
	}
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		return widget.mouseClicked(mouseX, mouseY, 0);
	}
	
	@Override
	public void onKeyPressed(int key, int scanCode, int modifiers) {
		widget.keyPressed(key, scanCode, modifiers);
		onChanged(widget.getText());
	}
	
	@Override
	public void onCharTyped(char codePoint, int modifiers) {
		widget.charTyped(codePoint, modifiers);
		onChanged(widget.getText());
	}
}
