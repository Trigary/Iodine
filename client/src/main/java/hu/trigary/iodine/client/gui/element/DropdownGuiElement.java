package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

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
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		width = buffer.readShort();
		editable = buffer.readBool();
		tooltip = buffer.readString();
		choices = new String[buffer.readShort()];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = buffer.readString();
		}
		selected = buffer.readShort();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, HEIGHT);
	}
	
	/**
	 * Should be called when the user clicked this element.
	 * Calls {@link #sendChangePacket(Consumer)} internally after doing sanity checks.
	 */
	protected final void onChanged() {
		if (editable && choices.length != 1) {
			sendChangePacket(b -> b.putShort((short) (selected + 1 == choices.length ? 0 : selected + 1)));
		}
	}
}
