package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.TextGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link TextGuiElement}.
 */
public class TextGuiElementImpl extends GuiElementImpl<TextGuiElement> implements TextGuiElement {
	private short width = 200;
	private String text = "";
	private Alignment alignment = Alignment.CENTER;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.TEXT, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	@NotNull
	@Override
	public Alignment getAlignment() {
		return alignment;
	}
	
	
	
	@NotNull
	@Override
	public TextGuiElementImpl setWidth(int width) {
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextGuiElementImpl setAlignment(@NotNull Alignment alignment) {
		this.alignment = alignment;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putShort(width);
		buffer.putString(text);
		buffer.putByte((byte) alignment.getNumber());
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {}
}
