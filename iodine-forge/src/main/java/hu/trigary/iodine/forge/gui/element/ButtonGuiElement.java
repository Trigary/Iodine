package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ButtonGuiElement extends GuiElement {
	private static final int HEIGHT = 20;
	private int width;
	private boolean editable;
	private String text;
	private GuiButton element;
	
	public ButtonGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.BUTTON, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
	}
	
	
	
	@Override
	public void update() {
		element = new GuiButton(getId(), getX(), getY(), width, HEIGHT, text);
		element.enabled = editable;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		element.drawButton(MC, mouseX, mouseY, partialTicks);
	}
	
	
	
	@Override
	public boolean onMousePressed(int mouseX, int mouseY) {
		if (element.mousePressed(MC, mouseX, mouseY)) {
			sendChangePacket(0, buffer -> {});
			return true;
		}
		return false;
	}
}
