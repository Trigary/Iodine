package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.util.IntPair;
import hu.trigary.iodine.client.IodineModBase;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class IodineOverlay extends GuiBase {
	private final byte anchor;
	private final short horizontalOffset;
	private final short verticalOffset;
	private byte drawPriority;
	
	public IodineOverlay(@NotNull IodineModBase mod, int id,
			byte anchor, short horizontalOffset, short verticalOffset) {
		super(mod, id);
		this.anchor = anchor;
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
	}
	
	
	
	@Contract(pure = true)
	public final byte getDrawPriority() {
		return drawPriority;
	}
	
	
	
	@Override
	protected final void deserializeStart(@NotNull ByteBuffer buffer) {
		drawPriority = buffer.get();
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected final IntPair calculatePosition(int screenWidth, int screenHeight, int guiWidth, int guiHeight) {
		switch (anchor) {
			case 1:
				return new IntPair(horizontalOffset,
						screenHeight - guiHeight + verticalOffset);
			case 3:
				return new IntPair(screenWidth - guiWidth + horizontalOffset,
						screenHeight - guiHeight + verticalOffset);
			case 7:
				return new IntPair(horizontalOffset,
						verticalOffset);
			case 9:
				return new IntPair(screenWidth - guiWidth + horizontalOffset,
						verticalOffset);
			case 2:
				return new IntPair(screenWidth / 2 - guiWidth / 2 + horizontalOffset,
						screenHeight - guiHeight + verticalOffset);
			case 4:
				return new IntPair(horizontalOffset,
						screenHeight / 2 - guiHeight / 2 + verticalOffset);
			case 6:
				return new IntPair(screenWidth - guiWidth + horizontalOffset,
						screenHeight / 2 - guiHeight / 2 + verticalOffset);
			case 8:
				return new IntPair(screenWidth / 2 - guiWidth / 2 + horizontalOffset,
						verticalOffset);
			case 5:
				return new IntPair(screenWidth / 2 - guiWidth / 2 + horizontalOffset,
						screenHeight / 2 - guiHeight / 2 + verticalOffset);
			default:
				throw new AssertionError("Invalid anchor value: " + anchor);
		}
	}
}
