package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class RadioButtonGuiElement extends GuiElement {
	private boolean editable;
	private boolean checked;
	
	public RadioButtonGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.RADIO_BUTTON, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = deserializeBoolean(buffer);
		checked = deserializeBoolean(buffer);
		int groupId = buffer.getInt(); //TODO use
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
