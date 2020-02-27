package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#RECTANGLE}.
 */
public abstract class RectangleGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected String tooltip;
	protected int color;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected RectangleGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		width = buffer.readShort();
		height = buffer.readShort();
		tooltip = buffer.readString();
		color = buffer.readInt();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	/**
	 * Should be called when the user clicked this element.
	 * Calls {@link #sendChangePacket(Consumer)} internally after doing sanity checks.
	 */
	protected final void onChanged() {
		sendChangePacket(b -> {});
	}
}
