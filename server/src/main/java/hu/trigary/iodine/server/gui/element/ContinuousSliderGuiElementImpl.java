package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.ContinuousSliderGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link ContinuousSliderGuiElement}.
 */
public class ContinuousSliderGuiElementImpl extends GuiElementImpl<ContinuousSliderGuiElement> implements ContinuousSliderGuiElement {
	private short width = 150;
	private short height;
	private boolean editable = true;
	private boolean verticalOrientation;
	private String text = "";
	private float progress;
	private ProgressedAction progressedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ContinuousSliderGuiElementImpl(@NotNull IodineRootImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.CONTINUOUS_SLIDER, internalId, id);
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
	public boolean isEditable() {
		return editable;
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
	public ContinuousSliderGuiElementImpl setWidth(int width) {
		Validate.isTrue(!verticalOrientation, "The width is only configurable in horizontal orientation");
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ContinuousSliderGuiElementImpl setHeight(int height) {
		Validate.isTrue(verticalOrientation, "The height is only configurable in vertical orientation");
		this.height = (short) height;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ContinuousSliderGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ContinuousSliderGuiElementImpl setOrientation(boolean vertical) {
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
	public ContinuousSliderGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ContinuousSliderGuiElementImpl setProgress(float progress) {
		Validate.isTrue(progress >= 0 && progress <= 1, "Progress must be at least 0 and at most 1");
		this.progress = progress;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ContinuousSliderGuiElementImpl onProgressed(@Nullable ProgressedAction action) {
		progressedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(verticalOrientation);
		buffer.putShort(verticalOrientation ? height : width);
		buffer.putBool(editable);
		buffer.putString(text);
		buffer.putFloat(progress);
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		if (!editable) {
			return;
		}
		
		float newProgress = message.getFloat();
		if (Float.compare(progress, newProgress) == 0 || newProgress < 0 || newProgress > 1) {
			return;
		}
		
		float oldProgress = progress;
		progress = newProgress;
		if (progressedAction == null) {
			getRoot().flagAndUpdate(this);
		} else {
			getRoot().flagAndAtomicUpdate(this, () -> progressedAction.accept(this, oldProgress, progress, player));
		}
	}
}
