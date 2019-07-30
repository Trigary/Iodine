package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TextFieldGuiElement extends GuiElement {
	private int width;
	private int height;
	private boolean editable;
	private String text;
	private Pattern regex;
	private int maxLength;
	private GuiTextField element;
	private boolean focused;
	
	public TextFieldGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.TEXT_FIELD, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		height = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		String rawRegex = BufferUtils.deserializeString(buffer);
		regex = rawRegex.isEmpty() ? null : Pattern.compile(rawRegex);
		maxLength =  buffer.get() & 0xFF;
	}
	
	
	
	@Override
	public void update() {
		element = new GuiTextField(getId(), MC.fontRenderer, getX(), getY(), width, height);
		element.setEnabled(editable);
		element.setFocused(focused);
		element.setText(text);
		if (regex != null) {
			//noinspection ConstantConditions
			element.setValidator(input -> regex.matcher(input).matches());
		}
		element.setMaxStringLength(maxLength);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		element.drawTextBox();
	}
	
	
	
	@Override
	public boolean onMousePressed(int mouseX, int mouseY) {
		boolean result = element.mouseClicked(mouseX, mouseY, 0);
		focused = element.isFocused();
		return result;
		/*boolean focused = element.isFocused();
		boolean result = element.mouseClicked(mouseX, mouseY, 0);
		if (focused && !element.isFocused() && !serverSideText.equals(text)) {
			byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
			sendChangePacket(4 + bytes.length, buffer -> buffer.putInt(bytes.length).put(bytes));
		}
		return result;*/
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if (element.textboxKeyTyped(typedChar, keyCode)) {
			String newText = element.getText();
			if (!text.equals(newText)) {
				text = newText;
				byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
				sendChangePacket(4 + bytes.length, buffer -> buffer.putInt(bytes.length).put(bytes));
			}
		}
	}
}
