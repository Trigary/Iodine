package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#CONTAINER_GRID}.
 */
public final class GridGuiContainer extends GuiContainer {
	private int[] childrenTemp;
	private GuiElement[] children;
	private int columnCount;
	private int rowCount;
	private int[] maxWidths;
	private int[] maxHeights;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public GridGuiContainer(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	private GuiElement getChild(int column, int row) {
		return children[column * rowCount + row];
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull InputBuffer buffer) {
		columnCount = buffer.readShort();
		rowCount = buffer.readShort();
		childrenTemp = new int[columnCount * rowCount];
		for (int i = 0; i < childrenTemp.length; i++) {
			childrenTemp[i] = buffer.readInt();
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
				maxWidth = Math.max(maxWidth, getChild(column, row).getTotalWidth());
			}
			maxWidths[column] = maxWidth;
			width += maxWidth;
		}
		
		int height = 0;
		maxHeights = new int[rowCount];
		for (int row = 0; row < rowCount; row++) {
			int maxHeight = 0;
			for (int column = 0; column < columnCount; column++) {
				maxHeight = Math.max(maxHeight, getChild(column, row).getTotalHeight());
			}
			maxHeights[row] = maxHeight;
			height += maxHeight;
		}
		
		return new IntPair(width, height);
	}
	
	@Override
	protected void setChildrenPositions(int offsetX, int offsetY) {
		int x = offsetX;
		for (int column = 0; column < columnCount; column++) {
			int y = offsetY;
			for (int row = 0; row < rowCount; row++) {
				getChild(column, row).setPosition(x, y);
				y += maxHeights[row];
			}
			x += maxWidths[column];
		}
		
		maxWidths = null;
		maxHeights = null;
	}
}
