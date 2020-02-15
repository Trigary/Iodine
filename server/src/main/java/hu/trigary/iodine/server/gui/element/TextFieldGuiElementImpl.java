package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.TextFieldGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * The implementation of {@link TextFieldGuiElement}.
 */
public class TextFieldGuiElementImpl extends GuiElementImpl<TextFieldGuiElement> implements TextFieldGuiElement {
	private short width = 200;
	private short height = 20;
	private boolean editable = true;
	private String tooltip = "";
	private String text = "";
	private String regex = "";
	private int maxLength = 32;
	private TextChangedAction textChangedAction;
	private Pattern compiledRegex;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextFieldGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.TEXT_FIELD, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Contract(pure = true)
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getTooltip() {
		return tooltip;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getRegex() {
		return regex;
	}
	
	@Contract(pure = true)
	@Override
	public int getMaxLength() {
		return maxLength;
	}
	
	
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setWidth(int width) {
		if (this.width != width) {
			this.width = (short) width;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setHeight(int height) {
		if (this.height != height) {
			this.height = (short) height;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setEditable(boolean editable) {
		if (this.editable != editable) {
			this.editable = editable;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setTooltip(@NotNull String tooltip) {
		if (!this.tooltip.equals(tooltip)) {
			this.tooltip = tooltip;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setText(@NotNull String text) {
		if (!this.text.equals(text)) {
			Validate.isTrue(isValid(compiledRegex, text), "The text must match the regex");
			this.text = text;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElement setRegex(@NotNull String regex) {
		if (!this.regex.equals(regex)) {
			Pattern tempRegex = regex.isEmpty() ? null : Pattern.compile(regex);
			Validate.isTrue(isValid(tempRegex, text), "The text must match the regex");
			this.regex = regex;
			compiledRegex = tempRegex;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElement setMaxLength(int maxLength) {
		if (this.maxLength != maxLength) {
			Validate.isTrue(maxLength > 0 && maxLength <= 250, "The max length must be positive and at most 250");
			Validate.isTrue(text.length() <= maxLength, "The text must not be longer than the max length");
			this.maxLength = maxLength;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl onChanged(@Nullable TextChangedAction action) {
		textChangedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putShort(width);
		buffer.putShort(height);
		buffer.putBool(editable);
		buffer.putString(tooltip);
		buffer.putString(text);
		buffer.putString(regex);
		buffer.putByte((byte) maxLength);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {
		if (!editable) {
			return;
		}
		
		String newText = buffer.readString(maxLength * 4);
		if (newText == null || text.equals(newText) || newText.length() > maxLength || !isValid(compiledRegex, newText)) {
			return;
		}
		
		String oldText = text;
		text = newText;
		if (textChangedAction == null) {
			getRoot().flagAndUpdate(this);
		} else {
			getRoot().flagAndAtomicUpdate(this, () -> textChangedAction.accept(this, oldText, text, player));
		}
	}
	
	@Contract(pure = true, value = "null, _ -> true")
	private static boolean isValid(@Nullable Pattern regex, @NotNull String text) {
		return regex == null || regex.matcher(text).matches();
	}
}
