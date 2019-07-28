package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ProgressBarGuiElement extends GuiElement {
	private boolean verticalOrientation;
	private String text;
	private int maxProgress;
	private int progress;
	
	public ProgressBarGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.PROGRESS_BAR, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		maxProgress = buffer.getInt();
		progress = buffer.getInt();
	}
	
	
	
	@Override
	public void update() {
		throw new NotImplementedException(""); //TODO boss bar style?
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	
	}
}
