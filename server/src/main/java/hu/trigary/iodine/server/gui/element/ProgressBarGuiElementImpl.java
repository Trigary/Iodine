package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.ProgressBarGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
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
	private float progress;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ProgressBarGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.PROGRESS_BAR, internalId, id);
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
	public float getProgress() {
		return progress;
	}
	
	
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setWidth(int width) {
		Validate.isTrue(!verticalOrientation, "The width is only configurable in horizontal orientation");
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setHeight(int height) {
		Validate.isTrue(verticalOrientation, "The height is only configurable in vertical orientation");
		this.height = (short) height;
		getRoot().flagAndUpdate(this);
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
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setProgress(float progress) {
		Validate.isTrue(progress >= 0 && progress <= 1, "Progress must be at least 0 and at most 1");
		this.progress = progress;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(verticalOrientation);
		buffer.putShort(verticalOrientation ? height : width);
		buffer.putString(text);
		buffer.putFloat(progress);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {}
}
