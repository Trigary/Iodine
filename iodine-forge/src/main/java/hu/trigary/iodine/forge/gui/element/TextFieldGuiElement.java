package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class TextFieldGuiElement extends GuiElement {
	private int width;
	private int height;
	private boolean editable;
	private String text;
	private Pattern regex;
	
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
	}
	
	@Override
	public Gui updateImpl() {
		GuiTextField field = new GuiTextField(getId(), MC.fontRenderer, getX(), getY(), width, height);
		field.setText(text);
		if (regex != null) {
			//noinspection ConstantConditions
			field.setValidator(input -> regex.matcher(input).matches());
		}
		return field;
	}
}
