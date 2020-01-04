package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.CheckboxGuiElement;
import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link CheckboxGuiElement}.
 */
public class CheckboxGuiElementImpl extends GuiElementImpl<CheckboxGuiElement> implements CheckboxGuiElement {
	private boolean editable = true;
	private boolean checked;
	private ClickedAction<CheckboxGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public CheckboxGuiElementImpl(@NotNull IodineRootImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.CHECKBOX, internalId, id);
	}
	
	
	
	@Contract(pure = true)
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@Contract(pure = true)
	@Override
	public boolean isChecked() {
		return checked;
	}
	
	
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setChecked(boolean checked) {
		this.checked = checked;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl onClicked(@Nullable ClickedAction<CheckboxGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(editable);
		buffer.putBool(checked);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		if (!editable) {
			return;
		}
		
		boolean received = BufferUtils.deserializeBoolean(message);
		if (checked == received) {
			return;
		}
		
		checked = !checked;
		if (clickedAction == null) {
			getRoot().flagAndUpdate(this);
		} else {
			getRoot().flagAndAtomicUpdate(this, () -> clickedAction.accept(this, player));
		}
	}
}
