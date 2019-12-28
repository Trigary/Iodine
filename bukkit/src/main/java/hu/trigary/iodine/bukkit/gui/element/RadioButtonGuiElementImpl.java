package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.RadioButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.bukkit.entity.Player;
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
	private boolean checked;
	private RadioButtonGroupData groupData;
	private UncheckedAction uncheckedAction;
	private CheckedAction checkedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public RadioButtonGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.RADIO_BUTTON, internalId, id);
		setGroupId(0);
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
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setChecked(boolean checked) {
		this.checked = checked;
		getGui().flagAndUpdate(this);
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
				getGui().flagOnly(groupData.checked);
			}
		}
		
		RadioButtonGuiElementImpl other = getGui().getAllElements().stream()
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
		getGui().flagAndUpdate(this);
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
		buffer.putBool(checked);
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (!editable || groupData.checked == this) {
			return;
		}
		
		RadioButtonGuiElementImpl oldChecked = groupData.checked;
		groupData.checked.checked = false;
		groupData.checked = this;
		checked = true;
		getGui().flagOnly(oldChecked);
		
		if (oldChecked.uncheckedAction == null && checkedAction == null) {
			getGui().flagAndUpdate(this);
		} else {
			getGui().flagAndAtomicUpdate(this, () -> {
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
			getGui().flagOnly(groupData.checked);
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
