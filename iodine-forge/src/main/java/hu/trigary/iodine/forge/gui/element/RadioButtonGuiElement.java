package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RadioButtonGuiElement extends GuiElement {
	private boolean editable;
	private boolean checked;
	private RadioButtonGroupData groupData;
	
	public RadioButtonGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.RADIO_BUTTON, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = deserializeBoolean(buffer);
		checked = deserializeBoolean(buffer);
		
		int groupId = buffer.getInt();
		groupData = getGui().getElements().stream()
				.filter(RadioButtonGuiElement.class::isInstance)
				.map(RadioButtonGuiElement.class::cast)
				.map(e -> e.groupData)
				.filter(data -> data.id == groupId)
				.findAny().orElseGet(() -> new RadioButtonGroupData(groupId));
		
		groupData.elements.add(this);
		if (checked) {
			groupData.checked = this;
		}
	}
	
	@Override
	public Gui updateImpl() {
	
	}
	
	
	
	private static class RadioButtonGroupData {
		final List<RadioButtonGuiElement> elements = new ArrayList<>();
		final int id;
		RadioButtonGuiElement checked;
		
		RadioButtonGroupData(int id) {
			this.id = id;
		}
	}
}
