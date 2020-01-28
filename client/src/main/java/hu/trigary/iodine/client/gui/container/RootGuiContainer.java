package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#CONTAINER_ROOT}.
 */
public final class RootGuiContainer extends GuiContainer {
	private final Map<Integer, Position> children = new HashMap<>();
	private boolean childrenDirty;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public RootGuiContainer(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull InputBuffer buffer) {
		children.clear();
		int count = buffer.readInt();
		for (int i = 0; i < count; i++) {
			children.put(buffer.readInt(), new Position(buffer.readShort(), buffer.readShort()));
		}
		childrenDirty = true;
	}
	
	@Override
	public void initialize() {
		if (childrenDirty) {
			children.forEach((id, position) -> position.element = getRoot().getElement(id));
			childrenDirty = false;
		}
		
		for (Position position : children.values()) {
			position.element.initialize();
		}
	}
	
	@NotNull
	@Override
	protected IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		int width = 0;
		int height = 0;
		for (Position position : children.values()) {
			position.element.calculateSize(screenWidth, screenHeight);
			width = Math.max(width, position.element.getTotalWidth() + position.x);
			height = Math.max(height, position.element.getTotalHeight() + position.y);
		}
		return new IntPair(width, height);
	}
	
	@Override
	protected void setChildrenPositions(int offsetX, int offsetY) {
		for (Position position : children.values()) {
			position.element.setPosition(offsetX + position.x, offsetY + position.y);
		}
	}
	
	
	
	private static class Position {
		final short x;
		final short y;
		GuiElement element;
		
		Position(short x, short y) {
			this.x = x;
			this.y = y;
		}
	}
}
