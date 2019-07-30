package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DropdownGuiElement extends GuiElement {
	private static final int HEIGHT = 20;
	private int width;
	private String[] choices;
	private boolean editable;
	private int selected;
	private GuiButton element;
	
	public DropdownGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.DROPDOWN, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		choices = new String[buffer.getInt()];
		Arrays.setAll(choices, i -> BufferUtils.deserializeString(buffer));
		selected = buffer.getInt();
	}
	
	
	
	@Override
	public void update() {
		element = new GuiButton(getId(), getX(), getY(), width, HEIGHT, choices[selected]);
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
		
		element.playPressSound(MC.getSoundHandler());
		selected = (selected + 1) % choices.length;
		element.displayString = choices[selected];
		sendChangePacket(4, buffer -> buffer.putInt(selected));
		//TODO short should be enough
		return true;
	}
}
