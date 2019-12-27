package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ProgressBarGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.IodineUtils;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link ProgressBarGuiElement}.
 */
public class ProgressBarGuiElementImpl extends GuiElementImpl<ProgressBarGuiElement> implements ProgressBarGuiElement {
	private short width = 150;
	private short height;
	private boolean verticalOrientation;
	private String text = "";
	private int maxProgress;
	private int progress;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ProgressBarGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.PROGRESS_BAR, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Contract(pure = true)
	@Override
	public boolean isVerticalOrientation() {
		return verticalOrientation;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	@Contract(pure = true)
	@Override
	public int getMaxProgress() {
		return maxProgress;
	}
	
	@Contract(pure = true)
	@Override
	public int getProgress() {
		return progress;
	}
	
	
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setWidth(int width) {
		Validate.isTrue(!verticalOrientation, "The width is only configurable in horizontal orientation");
		IodineUtils.validateWidth(width);
		this.width = (short) width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setHeight(int height) {
		Validate.isTrue(verticalOrientation, "The height is only configurable in vertical orientation");
		IodineUtils.validateHeight(height);
		this.height = (short) height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setOrientation(boolean vertical) {
		if (verticalOrientation == vertical) {
			return this;
		}
		
		short temp = width;
		//noinspection SuspiciousNameCombination
		width = height;
		height = temp;
		verticalOrientation = vertical;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setMaxProgress(int maxProgress) {
		Validate.isTrue(maxProgress >= 0, "Max progress must be at least 0");
		this.maxProgress = maxProgress;
		if (progress > maxProgress) {
			progress = maxProgress;
		}
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setProgress(int progress) {
		Validate.isTrue(progress >= 0 && progress <= maxProgress,
				"Progress must be at least 0 and at most maxProgress");
		this.progress = progress;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(verticalOrientation);
		buffer.putShort(verticalOrientation ? height : width);
		buffer.putString(text);
		buffer.putInt(maxProgress);
		buffer.putInt(progress);
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {}
}
