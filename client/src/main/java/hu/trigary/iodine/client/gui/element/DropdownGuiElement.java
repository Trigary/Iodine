package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DropdownGuiElement extends GuiElement {
	protected static final int HEIGHT = 20;
	protected int width;
	protected boolean editable;
	protected String[] choices;
	protected short selected;
	
	protected DropdownGuiElement(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull ByteBuffer buffer) {
		width = buffer.getShort();
		editable = BufferUtils.deserializeBoolean(buffer);
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
	
	protected final void onChanged() {
		if (editable && choices.length != 1) {
			sendChangePacket(4, b -> b.putShort((short) (selected + 1 == choices.length ? 0 : selected + 1)));
		}
	}
}
