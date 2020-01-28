package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#CONTAINER_LINEAR}.
 */
public final class LinearGuiContainer extends GuiContainer {
	private boolean verticalOrientation;
	private int[] childrenTemp;
	private GuiElement[] children;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public LinearGuiContainer(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull InputBuffer buffer) {
		verticalOrientation = buffer.readBool();
		childrenTemp = new int[buffer.readShort()];
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
		int width = 0;
		int height = 0;
		if (verticalOrientation) {
			for (GuiElement child : children) {
				child.calculateSize(screenWidth, screenHeight);
				width = Math.max(width, child.getTotalWidth());
				height += child.getTotalHeight();
			}
		} else {
			for (GuiElement child : children) {
				child.calculateSize(screenWidth, screenHeight);
				width += child.getTotalWidth();
				height = Math.max(height, child.getTotalHeight());
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
				height += child.getTotalHeight();
			}
		} else {
			int width = 0;
			for (GuiElement child : children) {
				child.setPosition(offsetX + width, offsetY);
				width += child.getTotalWidth();
			}
		}
	}
}
