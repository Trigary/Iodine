package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.RadioButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public RadioButtonGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
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
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setChecked(boolean checked) {
		this.checked = checked;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public RadioButtonGuiElementImpl setGroupId(int groupId) {
		//TODO I don't like this, make sure everything works, etc.
		//also re-check the documentation and implementation differences
		//eg. document what happens when a group runs out of elements
		//doc: the event does not fire
		
		if (groupData != null && groupData.id == groupId) {
			return this;
		}
		
		if (groupData != null) {
			groupData.elements.remove(this);
			if (!groupData.elements.isEmpty() && groupData.checked == this) {
				groupData.checked = groupData.elements.get(0);
				//TODO update the newly checked element
			}
		}
		
		RadioButtonGuiElementImpl other = gui.getAllElements().stream()
				.filter(RadioButtonGuiElementImpl.class::isInstance)
				.map(RadioButtonGuiElementImpl.class::cast)
				.filter(e -> e.groupData.id == groupId)
				.findAny().orElse(null);
		if (other == null) {
			groupData = new RadioButtonGroupData(groupId);
			groupData.checked = this;
		} else {
			groupData = other.groupData;
		}
		
		groupData.elements.add(this);
		gui.update();
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
	
	
	
	private static class RadioButtonGroupData {
		private final List<RadioButtonGuiElementImpl> elements = new ArrayList<>();
		final int id;
		RadioButtonGuiElementImpl checked;
		
		RadioButtonGroupData(int id) {
			this.id = id;
		}
	}
}
