package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.IodineMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Represents an overlay.
 */
public final class IodineOverlay extends IodineRoot {
	private final byte anchor;
	private final short horizontalOffset;
	private final short verticalOffset;
	private byte drawPriority;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link OverlayManager}.
	 *
	 * @param mod the mod instance
	 * @param id this instance's ID
	 * @param anchor the anchor number
	 * @param horizontalOffset the horizontal offset
	 * @param verticalOffset the vertical offset
	 */
	public IodineOverlay(@NotNull IodineMod mod, int id,
			byte anchor, short horizontalOffset, short verticalOffset) {
		super(mod, id);
		this.anchor = anchor;
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
	}
	
	
	
	/**
	 * Gets this overlay's draw priority.
	 * Should be used together with {@link #getId()} to order the overlays.
	 *
	 * @return the draw priority of this element
	 */
	@Contract(pure = true)
	public byte getDrawPriority() {
		return drawPriority;
	}
	
	
	
	@Override
	protected void deserializeStart(@NotNull ByteBuffer buffer) {
		drawPriority = buffer.get();
	}
	
	@Override
	protected void onElementRemoved(@NotNull GuiElement element) {}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected IntPair calculatePosition(int screenWidth, int screenHeight, int width, int height) {
		switch (anchor) {
			case 1:
				return new IntPair(horizontalOffset,
						screenHeight - height + verticalOffset);
			case 3:
				return new IntPair(screenWidth - width + horizontalOffset,
						screenHeight - height + verticalOffset);
			case 7:
				return new IntPair(horizontalOffset,
						verticalOffset);
			case 9:
				return new IntPair(screenWidth - width + horizontalOffset,
						verticalOffset);
			case 2:
				return new IntPair(screenWidth / 2 - width / 2 + horizontalOffset,
						screenHeight - height + verticalOffset);
			case 4:
				return new IntPair(horizontalOffset,
						screenHeight / 2 - height / 2 + verticalOffset);
			case 6:
				return new IntPair(screenWidth - width + horizontalOffset,
						screenHeight / 2 - height / 2 + verticalOffset);
			case 8:
				return new IntPair(screenWidth / 2 - width / 2 + horizontalOffset,
						verticalOffset);
			case 5:
				return new IntPair(screenWidth / 2 - width / 2 + horizontalOffset,
						screenHeight / 2 - height / 2 + verticalOffset);
			default:
				throw new AssertionError("Invalid anchor value: " + anchor);
		}
	}
	
	@Override
	protected void onUpdated() {}
}
