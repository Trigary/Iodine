package hu.trigary.iodine.forge.gui.container;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.GuiElement;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class LinearGuiContainer extends GuiElement {
	private int[] children;
	private boolean verticalOrientation;
	
	public LinearGuiContainer(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CONTAINER_GRID, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		verticalOrientation = deserializeBoolean(buffer);
		children = new int[buffer.getInt()];
		Arrays.setAll(children, i -> buffer.getInt());
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
