package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class CheckboxGuiElement extends GuiElement {
	protected static final int SIZE = 20;
	protected boolean editable;
	protected boolean checked;
	
	protected CheckboxGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		editable = BufferUtils.deserializeBoolean(buffer);
		checked = BufferUtils.deserializeBoolean(buffer);
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(SIZE, SIZE);
	}
	
	protected final void onChanged() {
		if (editable) {
			sendChangePacket(1, b -> BufferUtils.serializeBoolean(b, !checked));
		}
	}
}
