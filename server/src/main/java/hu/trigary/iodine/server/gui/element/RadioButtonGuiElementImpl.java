package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.RadioButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of {@link RadioButtonGuiElement}.
 */
public class RadioButtonGuiElementImpl extends GuiElementImpl<RadioButtonGuiElement> implements RadioButtonGuiElement {
	private boolean editable = true;
	private String tooltip = "";
	private boolean checked;
	private RadioButtonGroupData groupData;
	private UncheckedAction uncheckedAction;
	private CheckedAction checkedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public RadioButtonGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.RADIO_BUTTON, internalId, id);
		setGroupId(0);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getTooltip() {
		return tooltip;
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
	
	@Contract(pure = true)
	@Override
	public int getGroupId() {
		return groupData.id;
	}
	
	
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setChecked(boolean checked) {
		this.checked = checked;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setGroupId(int groupId) {
		if (groupData != null && groupData.id == groupId) {
			return this;
		}
		
		if (groupData != null) {
			groupData.elements.remove(this);
			if (groupData.checked == this && !groupData.elements.isEmpty()) {
				groupData.checked = groupData.elements.get(0);
				groupData.checked.checked = true;
				getRoot().flagOnly(groupData.checked);
			}
		}
		
		RadioButtonGuiElementImpl other = getRoot().getAllElements().stream()
				.filter(RadioButtonGuiElementImpl.class::isInstance)
				.map(RadioButtonGuiElementImpl.class::cast)
				.filter(e -> e.groupData.id == groupId)
				.findAny().orElse(null);
		if (other == null) {
			groupData = new RadioButtonGroupData(groupId);
			groupData.checked = this;
			checked = true;
		} else {
			groupData = other.groupData;
			checked = false;
		}
		
		groupData.elements.add(this);
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl onUnchecked(@Nullable UncheckedAction action) {
		uncheckedAction = action;
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl onChecked(@Nullable CheckedAction action) {
		checkedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(editable);
		buffer.putString(tooltip);
		buffer.putBool(checked);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		if (!editable || groupData.checked == this) {
			return;
		}
		
		RadioButtonGuiElementImpl oldChecked = groupData.checked;
		groupData.checked.checked = false;
		groupData.checked = this;
		checked = true;
		getRoot().flagOnly(oldChecked);
		
		if (oldChecked.uncheckedAction == null && checkedAction == null) {
			getRoot().flagAndUpdate(this);
		} else {
			getRoot().flagAndAtomicUpdate(this, () -> {
				if (oldChecked.uncheckedAction != null) {
					oldChecked.uncheckedAction.accept(this, oldChecked, player);
				}
				if (checkedAction != null) {
					checkedAction.accept(this, oldChecked, player);
				}
			});
		}
	}
	
	@Override
	public void onRemoved() {
		groupData.elements.remove(this);
		if (groupData.checked == this && !groupData.elements.isEmpty()) {
			groupData.checked = groupData.elements.get(0);
			groupData.checked.checked = true;
			getRoot().flagOnly(groupData.checked);
		}
	}
	
	
	
	private static class RadioButtonGroupData {
		final List<RadioButtonGuiElementImpl> elements = new ArrayList<>();
		final int id;
		RadioButtonGuiElementImpl checked;
		
		RadioButtonGroupData(int id) {
			this.id = id;
		}
	}
}
