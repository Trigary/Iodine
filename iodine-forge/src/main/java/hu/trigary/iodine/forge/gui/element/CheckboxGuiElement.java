package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.ClickableElement;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class CheckboxGuiElement extends GuiElement implements ClickableElement {
	private static final int SIZE = 11;
	private boolean editable;
	private boolean checked;
	private GuiCheckBox gui;
	
	public CheckboxGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CHECKBOX, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		editable = BufferUtils.deserializeBoolean(buffer);
		checked = BufferUtils.deserializeBoolean(buffer);
		
		if (editable) {
			getGui().registerClickable(this, SIZE, SIZE);
		}
	}
	
	@Override
	public Gui updateImpl() {
		gui = new GuiCheckBox(getId(), getX(), getY(), "", checked);
		return gui;
	}
	
	
	
	@Override
	public void onClicked() {
		checked = !checked;
		gui.setIsChecked(checked);
		sendChangePacket(1, buffer -> BufferUtils.serializeBoolean(buffer, checked));
	}
}
