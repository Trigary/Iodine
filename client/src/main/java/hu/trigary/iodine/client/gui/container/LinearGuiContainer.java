package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class LinearGuiContainer extends GuiContainer {
	private boolean verticalOrientation;
	private int[] childrenTemp;
	private GuiElement[] children;
	
	public LinearGuiContainer(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull ByteBuffer buffer) {
		verticalOrientation = BufferUtils.deserializeBoolean(buffer);
		childrenTemp = new int[buffer.getShort()];
		for (int i = 0; i < childrenTemp.length; i++) {
			childrenTemp[i] = buffer.getInt();
		}
	}
	
	@Override
	public void initialize() {
		if (childrenTemp != null) {
			children = resolveChildren(childrenTemp);
			childrenTemp = null;
		}
		
		for (GuiElement child : children) {
			child.initialize();
		}
	}
	
	@NotNull
	@Override
	protected IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		int width = 0;
		int height = 0;
		if (verticalOrientation) {
			for (GuiElement child : children) {
				child.calculateSize(screenWidth, screenHeight);
				width = Math.max(width, child.getWidth());
				height += child.getHeight();
			}
		} else {
			for (GuiElement child : children) {
				child.calculateSize(screenWidth, screenHeight);
				width += child.getWidth();
				height = Math.max(height, child.getHeight());
			}
		}
		return new IntPair(width, height);
	}
	
	@Override
	protected void setChildrenPositions(int offsetX, int offsetY) {
		if (verticalOrientation) {
			int height = 0;
			for (GuiElement child : children) {
				child.setPosition(offsetX, offsetY + height);
				height += child.getHeight();
			}
		} else {
			int width = 0;
			for (GuiElement child : children) {
				child.setPosition(offsetX + width, offsetY);
				width += child.getWidth();
			}
		}
	}
}
