package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class GridGuiContainer extends GuiContainer {
	private int[] childrenTemp;
	private GuiElement[] children;
	private int columnCount;
	private int rowCount;
	private int[] maxWidths;
	private int[] maxHeights;
	
	public GridGuiContainer(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	private GuiElement getChild(int column, int row) {
		return children[column * rowCount + row];
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull ByteBuffer buffer) {
		columnCount = buffer.getShort();
		rowCount = buffer.getShort();
		childrenTemp = new int[columnCount * rowCount];
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
		for (GuiElement child : children) {
			child.calculateSize(screenWidth, screenHeight);
		}
		
		int width = 0;
		maxWidths = new int[columnCount];
		for (int column = 0; column < columnCount; column++) {
			int maxWidth = 0;
			for (int row = 0; row < rowCount; row++) {
				maxWidth = Math.max(maxWidth, getChild(column, row).getWidth());
			}
			maxWidths[column] = maxWidth;
			width += maxWidth;
		}
		
		int height = 0;
		maxHeights = new int[rowCount];
		for (int row = 0; row < rowCount; row++) {
			int maxHeight = 0;
			for (int column = 0; column < columnCount; column++) {
				maxHeight = Math.max(maxHeight, getChild(column, row).getHeight());
			}
			maxHeights[row] = maxHeight;
			height += maxHeight;
		}
		
		return new IntPair(width, height);
	}
	
	@Override
	protected void setChildrenPositions(int offsetX, int offsetY) {
		int x = 0;
		for (int column = 0; column < columnCount; column++) {
			int y = 0;
			for (int row = 0; row < rowCount; row++) {
				getChild(column, row).setPosition(offsetX + x, offsetY + y);
				y += maxHeights[row];
			}
			column += maxWidths[column];
		}
		
		maxWidths = null;
		maxHeights = null;
	}
}
