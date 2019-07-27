package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class SliderGuiElement extends GuiElement {
	private static final int HEIGHT = 20;
	private int width;
	private boolean editable;
	private boolean verticalOrientation;
	private String text;
	private int maxProgress;
	private int progress;
	
	public SliderGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.SLIDER, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		maxProgress = buffer.getInt();
		progress = buffer.getInt();
	}
	
	@Override
	public Gui updateImpl() {
		GuiSlider slider = new GuiSlider(new GuiPageButtonList.GuiResponder() {
			@Override
			public void setEntryValue(int id, boolean value) {
			
			}
			
			@Override
			public void setEntryValue(int id, float value) {
			
			}
			
			@Override
			public void setEntryValue(int id, @NotNull String value) {
			
			}
		}, getId(), getX(), getY(), text, 0, maxProgress, progress, (id, name, value) -> name + ": " + value);
		slider.setWidth(width);
		//TODO no setheight, but HEIGHT exists
		return slider;
	}
}
