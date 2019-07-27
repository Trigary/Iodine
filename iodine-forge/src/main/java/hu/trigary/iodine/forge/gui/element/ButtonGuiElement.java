package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.ClickableElement;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ButtonGuiElement extends GuiElement implements ClickableElement {
	private static final int HEIGHT = 20;
	private int width;
	private boolean editable;
	private String text;
	
	public ButtonGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.BUTTON, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		
		if (editable) {
			getGui().registerClickable(this, width, HEIGHT);
		}
	}
	
	@Override
	public Gui updateImpl() {
		return new GuiButton(getId(), getX(), getY(), width, HEIGHT, text);
	}
	
	
	
	@Override
	public void onClicked() {
		sendChangePacket(0, buffer -> {});
	}
}
