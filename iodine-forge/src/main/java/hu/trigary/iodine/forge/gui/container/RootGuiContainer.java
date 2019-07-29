package hu.trigary.iodine.forge.gui.container;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.container.base.GuiContainer;
import hu.trigary.iodine.forge.gui.container.base.GuiParent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RootGuiContainer extends GuiContainer {
	private final Map<Integer, Position> children = new HashMap<>();
	private int left;
	private int top;
	
	public RootGuiContainer(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CONTAINER_ROOT, 0);
		Validate.isTrue(id == 0, "ID of RootGuiContainer must be 0");
	}
	
	
	
	@Override
	public int getLeft() {
		return left;
	}
	
	@Override
	public int getTop() {
		return top;
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		int count = buffer.getInt();
		for (int i = 0; i < count; i++) {
			children.put(buffer.getInt(), new Position(buffer.getShort(), buffer.getShort()));
		}
	}
	
	@Override
	public void initialize(@Nullable GuiParent parent, int xOffset, int yOffset) {
		super.initialize(new DummyGuiParent(), 0, 0);
		children.forEach((id, pos) -> getGui().getElement(id).initialize(this, pos.x, pos.y));
		children.clear();
	}
	
	public void onResolutionChanged(int left, int top) {
		this.left = left;
		this.top = top;
	}
	
	
	
	@Override
	public void update() {}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {}
	
	
	
	private static class Position {
		final int x;
		final int y;
		
		Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private static class DummyGuiParent implements GuiParent {
		@Override
		public int getLeft() {
			return 0;
		}
		
		@Override
		public int getTop() {
			return 0;
		}
	}
}
