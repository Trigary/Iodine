package hu.trigary.iodine.client.gui.element.base;

import hu.trigary.iodine.client.gui.container.base.GuiBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class GuiElement {
	private final short[] padding = new short[4];
	private final GuiBase gui;
	private final int id;
	private byte drawPriority;
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	
	protected GuiElement(@NotNull GuiBase gui, int id) {
		this.gui = gui;
		this.id = id;
	}
	
	
	
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	@Contract(pure = true)
	public final byte getDrawPriority() {
		return drawPriority;
	}
	
	
	
	public final void deserialize(@NotNull ByteBuffer buffer) {
		padding[0] = buffer.getShort();
		padding[0] = buffer.getShort();
		padding[0] = buffer.getShort();
		padding[0] = buffer.getShort();
		drawPriority = buffer.get();
		deserializeImpl(buffer);
	}
	
	protected abstract void deserializeImpl(@NotNull ByteBuffer buffer);
	
	public void initialize() {}
	
	public final void calculateSize(int screenWidth, int screenHeight) { //in case percentage based stuff is added
		int[] size = calculateSizeImpl(screenWidth, screenHeight);
		width = size[0] + padding[2] + padding[3];
		height = size[1] + padding[0] + padding[1];
	}
	
	protected abstract int[] calculateSizeImpl(int screenWidth, int screenHeight);
	
	public void setPosition(int x, int y) {
		positionX = x + padding[2];
		positionY = y + padding[0];
	}
	
	public final void update() {
		updateImpl(width, height, positionX, positionY);
	}
	
	protected abstract void updateImpl(int width, int height, int positionX, int positionY);
	
	public final void draw(int mouseX, int mouseY, float partialTicks) {
		drawImpl(width, height, positionX, positionY, mouseX, mouseY, partialTicks);
	}
	
	protected abstract void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks);
}
