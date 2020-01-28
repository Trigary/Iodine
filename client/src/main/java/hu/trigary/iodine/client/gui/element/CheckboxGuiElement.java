package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#CHECKBOX}.
 */
public abstract class CheckboxGuiElement extends GuiElement {
	protected static final int SIZE = 20;
	protected boolean editable;
	protected String tooltip;
	protected boolean checked;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected CheckboxGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		editable = buffer.readBool();
		tooltip = buffer.readString();
		checked = buffer.readBool();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(SIZE, SIZE);
	}
	
	/**
	 * Should be called when the user clicked this element.
	 * Calls {@link #sendChangePacket(Consumer)} internally after doing sanity checks.
	 */
	protected final void onChanged() {
		if (editable) {
			sendChangePacket(b -> b.putBool(!checked));
		}
	}
}
