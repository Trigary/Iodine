package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
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
		verticalOrientation = deserializeBoolean(buffer);
		text = deserializeString(buffer);
		maxProgress = buffer.getInt();
		progress = buffer.getInt();
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
