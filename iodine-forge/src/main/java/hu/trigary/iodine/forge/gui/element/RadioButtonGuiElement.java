package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.apache.commons.lang3.NotImplementedException;
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
		editable = BufferUtils.deserializeBoolean(buffer);
		checked = BufferUtils.deserializeBoolean(buffer);
		
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
	public void update() {
		throw new NotImplementedException(""); //TODO a custom texture is needed for this I think
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	
	}
	
	
	
	@Override
	public boolean onMousePressed(int mouseX, int mouseY) {
		/*if (!element.mousePressed(MC, mouseX, mouseY)) {
			return false;
		}
		
		return true;*/
		throw new NotImplementedException("");
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
