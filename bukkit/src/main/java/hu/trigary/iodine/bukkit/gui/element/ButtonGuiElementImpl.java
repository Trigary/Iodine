package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends GuiElementImpl<ButtonGuiElement> implements ButtonGuiElement {
	private short width = 100;
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
	public ButtonGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
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
		this.width = (short) width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl onClicked(@Nullable ClickedAction<ButtonGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort(width);
		buffer.putBool(editable);
		buffer.putString(text);
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (editable && clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
