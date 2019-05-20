package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ButtonGuiElement extends GuiElement {
	private boolean editable;
	private String text;
	
	public ButtonGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.BUTTON, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = deserializeBoolean(buffer);
		text = deserializeString(buffer);
	}
	
	@Override
	public Gui updateImpl() {
		return new GuiButton(getId(), 0, 0, text);
	}
}
