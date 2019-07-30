package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.TextFieldGuiElement;
import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

/**
 * The implementation of {@link TextFieldGuiElement}.
 */
public class TextFieldGuiElementImpl extends GuiElementImpl<TextFieldGuiElement> implements TextFieldGuiElement {
	private int width = 200;
	private int height = 20;
	private boolean editable = true;
	private String text = "";
	private String regex = "";
	private int maxLength = 32;
	private TextChangedAction textChangedAction;
	private Pattern compiledRegex;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextFieldGuiElementImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.TEXT_FIELD, internalId, id);
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
		Validate.isTrue(width > 0 && width <= Short.MAX_VALUE, "The width must be positive and at most Short.MAX_VALUE");
		this.width = width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setHeight(int height) {
		Validate.isTrue(height > 0 && height <= Short.MAX_VALUE, "The height must be positive and at most Short.MAX_VALUE");
		this.height = height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElement setRegex(@NotNull String regex) {
		Validate.notNull(regex, "The regex must be non-null");
		this.regex = regex;
		compiledRegex = regex.isEmpty() ? null : Pattern.compile(regex);
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElement setMaxLength(int maxLength) {
		Validate.isTrue(maxLength > 0 && maxLength <= 250, "The max length must be positive and at most 250");
		this.maxLength = maxLength;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextFieldGuiElementImpl onChanged(@Nullable TextChangedAction action) {
		textChangedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ByteBuffer buffer) {
		buffer.putShort((short) width);
		buffer.putShort((short) height);
		BufferUtils.serializeBoolean(buffer, editable);
		BufferUtils.serializeString(buffer, text);
		BufferUtils.serializeString(buffer, regex);
		buffer.put((byte) maxLength);
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (!editable) {
			return;
		}
		
		String newText = BufferUtils.deserializeString(message);
		if (text.equals(newText)
				|| newText.length() > maxLength
				|| (compiledRegex != null && !compiledRegex.matcher(newText).matches())) {
			return;
		}
		
		String oldText = text;
		text = newText;
		if (textChangedAction == null) {
			getGui().flagAndUpdate(this);
		} else {
			getGui().flagAndAtomicUpdate(this, () -> textChangedAction.accept(this, oldText, text, player));
		}
	}
}
