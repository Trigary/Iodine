package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class TextGuiElement extends GuiElement {
	private String text;
	
	public TextGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.TEXT, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		text = deserializeString(buffer);
	}
	
	@Override
	public Gui updateImpl() {
		GuiLabel label = new GuiLabel(MC.fontRenderer, getId(), 0, 0, 200, 20, 0xFFFFFF);
		label.setCentered();
		label.addLine(text);
		return label;
	}
}
