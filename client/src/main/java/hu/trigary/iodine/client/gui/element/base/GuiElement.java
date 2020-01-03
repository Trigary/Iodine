package hu.trigary.iodine.client.gui.element.base;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public abstract class GuiElement {
	private final short[] padding = new short[4];
	private final GuiBase gui;
	private final int id;
	private byte drawPriority;
	private int elementWidth;
	private int elementHeight;
	private int totalWidth;
	private int totalHeight;
	private int positionX;
	private int positionY;
	
	protected GuiElement(@NotNull GuiBase gui, int id) {
		this.gui = gui;
		this.id = id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final GuiBase getGui() {
		return gui;
	}
	
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	@Contract(pure = true)
	public final byte getDrawPriority() {
		return drawPriority;
	}
	
	@Contract(pure = true)
	public final int getTotalWidth() {
		return totalWidth;
	}
	
	@Contract(pure = true)
	public final int getTotalHeight() {
		return totalHeight;
	}
	
	
	
	public final void deserialize(@NotNull ByteBuffer buffer) {
		padding[0] = buffer.getShort();
		padding[1] = buffer.getShort();
		padding[2] = buffer.getShort();
		padding[3] = buffer.getShort();
		drawPriority = buffer.get();
		deserializeImpl(buffer);
	}
	
	protected abstract void deserializeImpl(@NotNull ByteBuffer buffer);
	
	public void initialize() {}
	
	
	
	public final void calculateSize(int screenWidth, int screenHeight) { //in case percentage based stuff is added
		IntPair size = calculateSizeImpl(screenWidth, screenHeight);
		elementWidth = size.getX();
		elementHeight = size.getY();
		totalWidth = elementWidth + padding[2] + padding[3];
		totalHeight = elementHeight + padding[0] + padding[1];
	}
	
	@NotNull
	protected abstract IntPair calculateSizeImpl(int screenWidth, int screenHeight);
	
	public final void setPosition(int x, int y) {
		positionX = x + padding[2];
		positionY = y + padding[0];
		setChildrenPositions(positionX, positionY);
	}
	
	protected void setChildrenPositions(int offsetX, int offsetY) {}
	
	public final void update() {
		updateImpl(elementWidth, elementHeight, positionX, positionY);
	}
	
	protected abstract void updateImpl(int width, int height, int positionX, int positionY);
	
	
	
	public final void draw(int mouseX, int mouseY, float partialTicks) {
		drawImpl(elementWidth, elementHeight, positionX, positionY, mouseX, mouseY, partialTicks);
	}
	
	protected abstract void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks);
	
	protected final void sendChangePacket(int dataLength, @NotNull Consumer<ByteBuffer> dataProvider) {
		getGui().getMod().getLogger().debug("GUI > {} sending change packet in {}", id, gui.getId());
		getGui().getMod().getNetwork().send(PacketType.CLIENT_GUI_CHANGE, dataLength + 8, buffer -> {
			buffer.putInt(gui.getId());
			buffer.putInt(id);
			dataProvider.accept(buffer);
		});
	}
	
	
	
	public void setFocused(boolean focused) {}
	
	public boolean onMousePressed(double mouseX, double mouseY) {
		return false; //returns true -> is now focused
	}
	
	public void onMouseReleased(double mouseX, double mouseY) {
		//only if focused
	}
	
	public void onMouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
		//only if focused
	}
	
	public void onKeyPressed(int key, int scanCode, int modifiers) {
		//only if focused
	}
	
	public void onKeyReleased(int key, int scanCode, int modifiers) {
		//only if focused
	}
	
	public void onCharTyped(char codePoint, int modifiers) {
		//only if focused
	}
}
