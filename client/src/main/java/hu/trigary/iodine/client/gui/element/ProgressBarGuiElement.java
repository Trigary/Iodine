package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class ProgressBarGuiElement extends GuiElement {
	private static final int SIZE = 20;
	protected int width;
	protected int height;
	protected boolean verticalOrientation;
	protected String text;
	protected float progress;
	
	protected ProgressBarGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		width = verticalOrientation ? SIZE : buffer.getShort();
		height = verticalOrientation ? buffer.getShort() : SIZE;
		text = BufferUtils.deserializeString(buffer);
		progress = buffer.getFloat();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
}
