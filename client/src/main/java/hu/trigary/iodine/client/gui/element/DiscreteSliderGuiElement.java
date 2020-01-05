package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#DISCRETE_SLIDER}.
 */
public abstract class DiscreteSliderGuiElement extends GuiElement {
	private static final int SIZE = 20;
	protected int width;
	protected int height;
	protected boolean editable;
	protected boolean verticalOrientation;
	protected String text;
	protected short maxProgress;
	protected short progress;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected DiscreteSliderGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		width = verticalOrientation ? SIZE : buffer.getShort();
		height = verticalOrientation ? buffer.getShort() : SIZE;
		editable = BufferUtils.deserializeBoolean(buffer);
		text = BufferUtils.deserializeString(buffer);
		maxProgress = buffer.getShort();
		progress = buffer.getShort();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	/**
	 * Should be called when the user moved and released this slider.
	 * Calls {@link #sendChangePacket(int, Consumer)} internally after doing sanity checks.
	 *
	 * @param newProgress the new progress
	 */
	protected final void onChanged(int newProgress) {
		if (editable && progress != newProgress && newProgress >= 0 && newProgress <= maxProgress) {
			sendChangePacket(4, b -> b.putShort((short) newProgress));
		}
	}
}
