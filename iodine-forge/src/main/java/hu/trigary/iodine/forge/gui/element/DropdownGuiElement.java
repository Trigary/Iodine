package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DropdownGuiElement extends GuiElement {
	private String[] choices;
	private boolean editable;
	private int selected;
	
	public DropdownGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.DROPDOWN, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = deserializeBoolean(buffer);
		choices = new String[buffer.getInt()];
		Arrays.setAll(choices, i -> deserializeString(buffer));
		selected = buffer.getInt();
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
