package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#TEXT}.
 */
public abstract class TextGuiElement extends GuiElement {
	protected static final int HEIGHT = 20;
	protected int width;
	protected String text;
	protected byte alignment;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected TextGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		text = BufferUtils.deserializeString(buffer);
		alignment = buffer.get();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, HEIGHT);
	}
}
