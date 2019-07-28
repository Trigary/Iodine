package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class CheckboxGuiElement extends GuiElement {
	private static final int SIZE = 11;
	private boolean editable;
	private boolean checked;
	private GuiCheckBox element;
	
	public CheckboxGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CHECKBOX, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = BufferUtils.deserializeBoolean(buffer);
		checked = BufferUtils.deserializeBoolean(buffer);
	}
	
	
	
	@Override
	public void update() {
		element = new GuiCheckBox(getId(), getX(), getY(), "", checked);
		element.enabled = editable;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		element.drawButton(MC, mouseX, mouseY, partialTicks);
	}
	
	
	
	@Override
	public boolean onMousePressed(int mouseX, int mouseY) {
		if (!element.mousePressed(MC, mouseX, mouseY)) {
			return false;
		}
		
		checked = !checked;
		element.setIsChecked(checked);
		sendChangePacket(1, buffer -> BufferUtils.serializeBoolean(buffer, checked));
		return true;
	}
}
