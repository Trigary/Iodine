package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class TextGuiElement extends GuiElement {
	private int width;
	private int height;
	private String text;
	private GuiLabel element;
	
	public TextGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.TEXT, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		height = buffer.getShort();
		text = BufferUtils.deserializeString(buffer);
	}
	
	
	
	@Override
	public void update() {
		element = new GuiLabel(MC.fontRenderer, getId(), getX(), getY(), width, height, 0xFFFFFF);
		//label.setCentered();
		element.addLine(text);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		element.drawLabel(MC, mouseX, mouseY);
	}
}
