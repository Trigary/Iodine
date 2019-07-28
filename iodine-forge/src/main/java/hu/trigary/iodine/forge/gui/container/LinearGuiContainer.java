package hu.trigary.iodine.forge.gui.container;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.container.base.GuiContainer;
import hu.trigary.iodine.forge.gui.container.base.GuiParent;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class LinearGuiContainer extends GuiContainer {
	private int[] childrenTemp;
	private GuiElement[] children;
	private boolean verticalOrientation;
	
	public LinearGuiContainer(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CONTAINER_GRID, id);
		throw new NotImplementedException("");
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		childrenTemp = new int[buffer.getInt()];
		Arrays.setAll(childrenTemp, i -> buffer.getInt());
	}
	
	@Override
	public void initialize(@NotNull GuiParent parent, int xOffset, int yOffset) {
		super.initialize(parent, xOffset, yOffset);
		children = resolveChildren(childrenTemp);
		childrenTemp = null;
		
		/*for (GuiElement child : children) {
			child.initialize(this, xOffset, yOffset);
		}*/
	}
	
	@Override
	public void update() {
		throw new NotImplementedException("");
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	
	}
	
	@Override
	public int getLeft() {
		return 0;
	}
	
	@Override
	public int getTop() {
		return 0;
	}
}
