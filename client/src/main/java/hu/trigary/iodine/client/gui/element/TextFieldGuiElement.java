package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public abstract class TextFieldGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected boolean editable;
	protected String text;
	protected Pattern regex;
	protected int maxLength;
	
	protected TextFieldGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		height = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		String regexString = BufferUtils.deserializeString(buffer);
		regex = regexString.isEmpty() ? null : Pattern.compile(regexString);
		maxLength = buffer.get();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	protected void onChanged(@NotNull String newText) {
		if (editable && !text.equals(newText) && newText.length() <= maxLength
				&& (regex == null || regex.matcher(newText).matches())) {
			byte[] array = BufferUtils.serializeString(newText);
			sendChangePacket(array.length, b -> BufferUtils.serializeString(b, array));
		}
	}
}
