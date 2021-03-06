package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.CheckboxGuiElement;
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
 * The implementation of {@link CheckboxGuiElement}.
 */
public class CheckboxGuiElementImpl extends GuiElementImpl<CheckboxGuiElement> implements CheckboxGuiElement {
	private boolean editable = true;
	private String tooltip = "";
	private boolean checked;
	private ClickedAction<CheckboxGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public CheckboxGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.CHECKBOX, internalId, id);
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
	
	@Contract(pure = true)
	@Override
	public boolean isChecked() {
		return checked;
	}
	
	
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setEditable(boolean editable) {
		if (this.editable != editable) {
			this.editable = editable;
			getRoot().flagAndUpdate(this);
		}
		return this;
	}
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setTooltip(@NotNull String tooltip) {
		if (!this.tooltip.equals(tooltip)) {
			this.tooltip = tooltip;
			getRoot().flagAndUpdate(this);
		}
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
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putBool(editable);
		buffer.putString(tooltip);
		buffer.putBool(checked);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {
		if (!editable) {
			return;
		}
		
		boolean received = buffer.readBool();
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
