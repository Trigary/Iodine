package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.ClickableElement;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RadioButtonGuiElement extends GuiElement implements ClickableElement {
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
		
		if (editable) {
			getGui().registerClickable(this, -1, -1);
		}
	}
	
	@Override
	public Gui updateImpl() {
		throw new NotImplementedException(""); //TODO a custom texture is needed for this I think
	}
	
	
	
	@Override
	public void onClicked() {
	
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
