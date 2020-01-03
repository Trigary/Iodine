package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class RectangleGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected int color;
	
	protected RectangleGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		height = buffer.getShort();
		color = (0xFF << 24) | ((buffer.get() & 0xFF) << 16) | ((buffer.get() & 0xFF) << 8) | (buffer.get() & 0xFF);
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	protected final void onChanged() {
		sendChangePacket(0, b -> {});
	}
}
