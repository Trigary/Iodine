package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#DROPDOWN}.
 */
public abstract class DropdownGuiElement extends GuiElement {
	protected static final int HEIGHT = 20;
	protected int width;
	protected boolean editable;
	protected String tooltip;
	protected String[] choices;
	protected short selected;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected DropdownGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
		tooltip = BufferUtils.deserializeString(buffer);
		choices = new String[buffer.getShort()];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = BufferUtils.deserializeString(buffer);
		}
		selected = buffer.getShort();
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
		if (editable && choices.length != 1) {
			sendChangePacket(4, b -> b.putShort((short) (selected + 1 == choices.length ? 0 : selected + 1)));
		}
	}
}
