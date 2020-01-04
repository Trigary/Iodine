package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class ImageGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected byte[] image;
	
	protected ImageGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		height = buffer.getShort();
		image = new byte[buffer.getInt()];
		buffer.get(image);
		throw new RuntimeException("not implemented");
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
