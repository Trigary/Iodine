package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#TEXT_FIELD}.
 */
public abstract class TextFieldGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected boolean editable;
	protected String tooltip;
	protected String text;
	protected Pattern regex;
	protected int maxLength;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected TextFieldGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		height = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		tooltip = BufferUtils.deserializeString(buffer);
		text = BufferUtils.deserializeString(buffer);
		String regexString = BufferUtils.deserializeString(buffer);
		regex = regexString.isEmpty() ? null : Pattern.compile(regexString);
		maxLength = buffer.get() & 0xFF;
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	/**
	 * Should be called when the user changed the text in this element.
	 * Calls {@link #sendChangePacket(int, Consumer)} internally after doing sanity checks.
	 *
	 * @param newText the new text in this element
	 */
	protected void onChanged(@NotNull String newText) {
		if (editable && !text.equals(newText) && newText.length() <= maxLength
				&& (regex == null || regex.matcher(newText).matches())) {
			byte[] array = BufferUtils.serializeString(newText);
			sendChangePacket(array.length + 2, buffer -> {
				buffer.putShort((short) array.length);
				buffer.put(array);
			});
		}
	}
}
