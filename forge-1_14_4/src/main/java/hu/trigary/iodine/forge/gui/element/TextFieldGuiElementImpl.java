package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.TextFieldGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.NotNull;

public class TextFieldGuiElementImpl extends TextFieldGuiElement {
	private TextFieldWidget widget;
	
	public TextFieldGuiElementImpl(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		//noinspection resource
		widget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, positionX, positionY, width, height, text);
		widget.active = editable;
		widget.setText(text);
		widget.setMaxStringLength(maxLength);
		widget.setValidator(s -> regex.matcher(s).matches());
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
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
