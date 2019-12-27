package hu.trigary.iodine.client.gui.container;

import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.container.base.GuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class RootGuiContainer extends GuiContainer {
	private final Map<Integer, Position> children = new HashMap<>();
	
	public RootGuiContainer(@NotNull GuiBase gui, int id) {
		super(gui, id);
	}
	
	
	
	@Override
	protected void deserializeImpl(@NotNull ByteBuffer buffer) {
		children.clear();
		int count = buffer.getInt();
		for (int i = 0; i < count; i++) {
			children.put(buffer.getInt(), new Position(buffer.getShort(), buffer.getShort()));
		}
	}
	
	@Override
	public void initialize() {
		children.forEach((id, position) -> position.element = getGui().getElement(id));
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
			width = Math.max(width, position.element.getWidth() + position.x);
			height = Math.max(height, position.element.getHeight() + position.y);
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
