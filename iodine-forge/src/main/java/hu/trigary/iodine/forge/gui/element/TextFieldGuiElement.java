package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class TextFieldGuiElement extends GuiElement {
	private boolean editable;
	private String text;
	
	public TextFieldGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.TEXT_FIELD, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = deserializeBoolean(buffer);
		text = deserializeString(buffer);
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
