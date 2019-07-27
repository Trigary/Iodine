package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
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

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends GuiElementImpl<ButtonGuiElement> implements ButtonGuiElement {
	private int width = 100;
	private boolean editable = true;
	private String text = "";
	private ClickedAction<ButtonGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ButtonGuiElementImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.BUTTON, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
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
	@Override
	public ButtonGuiElementImpl setWidth(int width) {
		Validate.isTrue(width > 0 && width <= Short.MAX_VALUE, "The width must be positive and at most Short.MAX_VALUE");
		this.width = width;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl onClicked(@Nullable ClickedAction<ButtonGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serialize(@NotNull ByteBuffer buffer) {
		super.serialize(buffer);
		buffer.putShort((short) width);
		BufferUtils.serializeBoolean(buffer, editable);
		BufferUtils.serializeString(buffer, text);
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (editable && clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
