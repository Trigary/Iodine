package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends GuiElementImpl<ButtonGuiElement> implements ButtonGuiElement {
	private short width = 100;
	private boolean editable = true;
	private String tooltip = "";
	private String text = "";
	private ClickedAction<ButtonGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ButtonGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.BUTTON, internalId, id);
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
	@Override
	public ButtonGuiElementImpl setWidth(int width) {
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl onClicked(@Nullable ClickedAction<ButtonGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putShort(width);
		buffer.putBool(editable);
		buffer.putString(tooltip);
		buffer.putString(text);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {
		if (editable && clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
