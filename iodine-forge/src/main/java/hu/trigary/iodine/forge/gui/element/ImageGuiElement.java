package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ImageGuiElement extends GuiElement {
	public ImageGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.IMAGE, id);
		throw new NotImplementedException("");
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		throw new NotImplementedException("");
	}
	
	
	
	@Override
	public void update() {
		throw new NotImplementedException("");
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	
	}
	
	
	
	@Override
	public boolean onMousePressed(int mouseX, int mouseY) {
		/*if (element.mousePressed(MC, mouseX, mouseY)) {
			sendChangePacket(0, buffer -> {});
			return true;
		}
		return false;*/
		throw new NotImplementedException("");
	}
}
