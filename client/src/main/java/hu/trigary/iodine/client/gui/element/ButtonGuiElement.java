package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#BUTTON}.
 */
public abstract class ButtonGuiElement extends GuiElement {
	protected static final int HEIGHT = 20;
	protected int width;
	protected boolean editable;
	protected String text;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected ButtonGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, HEIGHT);
	}
	
	/**
	 * Should be called when the user clicked this element.
	 * Calls {@link #sendChangePacket(int, Consumer)} internally after doing sanity checks.
	 */
	protected final void onChanged() {
		if (editable) {
			sendChangePacket(0, b -> {});
		}
	}
}
