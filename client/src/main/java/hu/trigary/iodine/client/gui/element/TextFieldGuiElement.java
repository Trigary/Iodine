package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

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
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		width = buffer.readShort();
		height = buffer.readShort();
		editable = buffer.readBool();
		tooltip = buffer.readString();
		text = buffer.readString();
		String regexString = buffer.readString();
		regex = regexString.isEmpty() ? null : Pattern.compile(regexString);
		maxLength = buffer.readByte() & 0xFF;
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	/**
	 * Should be called when the user changed the text in this element.
	 * Calls {@link #sendChangePacket(Consumer)} internally after doing sanity checks.
	 *
	 * @param newText the new text in this element
	 */
	protected void onChanged(@NotNull String newText) {
		if (editable && !text.equals(newText) && newText.length() <= maxLength
				&& (regex == null || regex.matcher(newText).matches())) {
			sendChangePacket(b -> b.putString(newText));
		}
	}
}
