package hu.trigary.iodine.forge.gui.element;

import com.google.common.base.Charsets;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class TextGuiElement extends GuiElement {
	private String text = "";
	
	public TextGuiElement(@NotNull IodineGui gui, @NotNull GuiElementType type, int id) {
		super(gui, type, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public String getText() {
		return text;
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		byte[] textBytes = new byte[buffer.getInt()];
		buffer.get(textBytes);
		text = new String(textBytes, Charsets.UTF_8);
	}
	
	@Override
	public Gui updateImpl() {
		GuiLabel label = new GuiLabel(MC.fontRenderer, getId(), 0, 0, 200, 20, 0xFFFFFF);
		label.setCentered();
		label.addLine(text);
		return label;
	}
}
