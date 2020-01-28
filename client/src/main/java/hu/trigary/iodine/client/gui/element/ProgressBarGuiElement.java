package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#PROGRESS_BAR}.
 */
public abstract class ProgressBarGuiElement extends GuiElement {
	private static final int SIZE = 10;
	protected boolean verticalOrientation;
	protected int width;
	protected int height;
	protected String tooltip;
	protected float progress;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected ProgressBarGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		verticalOrientation = buffer.readBool();
		width = verticalOrientation ? SIZE : buffer.readShort();
		height = verticalOrientation ? buffer.readShort() : SIZE;
		tooltip = buffer.readString();
		progress = buffer.readFloat();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
}
