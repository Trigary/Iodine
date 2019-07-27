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
import java.util.Arrays;

public class DropdownGuiElement extends GuiElement implements ClickableElement {
	private static final int HEIGHT = 20;
	private int width;
	private String[] choices;
	private boolean editable;
	private int selected;
	private GuiButton gui;
	
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
		
		if (editable) {
			getGui().registerClickable(this, width, HEIGHT);
		}
	}
	
	@Override
	public Gui updateImpl() {
		gui = new GuiButton(getId(), getX(), getY(), width, HEIGHT, choices[selected]);
		return gui;
	}
	
	
	
	@Override
	public void onClicked() {
		selected = (selected + 1) % choices.length;
		gui.displayString = choices[selected];
		sendChangePacket(4, buffer -> buffer.putInt(selected));
		//TODO 1 byte should be enough, especially if we use the whole range, not just half
	}
}
