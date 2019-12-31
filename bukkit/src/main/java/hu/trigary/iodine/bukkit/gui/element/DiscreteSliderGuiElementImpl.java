package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.DiscreteSliderGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link DiscreteSliderGuiElement}.
 */
public class DiscreteSliderGuiElementImpl extends GuiElementImpl<DiscreteSliderGuiElement> implements DiscreteSliderGuiElement {
	private short width = 150;
	private short height;
	private boolean editable = true;
	private boolean verticalOrientation;
	private String text = "";
	private short minProgress;
	private short maxProgress = 100;
	private short progress;
	private ProgressedAction progressedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public DiscreteSliderGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.DISCRETE_SLIDER, internalId, id);
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
	public int getMinProgress() {
		return minProgress;
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
	public DiscreteSliderGuiElementImpl setWidth(int width) {
		Validate.isTrue(!verticalOrientation, "The width is only configurable in horizontal orientation");
		this.width = (short) width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setHeight(int height) {
		Validate.isTrue(verticalOrientation, "The height is only configurable in vertical orientation");
		this.height = (short) height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setOrientation(boolean vertical) {
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
	public DiscreteSliderGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setMinProgress(int minProgress) {
		Validate.isTrue(minProgress <= maxProgress, "Min progress must be at most max progress");
		this.minProgress = (short) minProgress;
		if (progress < minProgress) {
			progress = this.minProgress;
		}
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setMaxProgress(int maxProgress) {
		Validate.isTrue(maxProgress >= minProgress, "Max progress must be at least min progress");
		this.maxProgress = (short) maxProgress;
		if (progress > maxProgress) {
			progress = this.maxProgress;
		}
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setProgress(int progress) {
		Validate.isTrue(progress >= 0 && progress <= maxProgress,
				"Progress must be at least 0 and at most maxProgress");
		this.progress = (short) progress;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl onProgressed(@Nullable ProgressedAction action) {
		progressedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putBool(verticalOrientation);
		buffer.putShort(verticalOrientation ? height : width);
		buffer.putBool(editable);
		buffer.putString(text);
		buffer.putShort((short) (maxProgress - minProgress));
		buffer.putShort((short) (progress - minProgress));
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (!editable) {
			return;
		}
		
		short newProgress = (short) (message.getShort() + minProgress);
		if (progress == newProgress || newProgress < 0 || newProgress > maxProgress) {
			return;
		}
		
		int oldProgress = progress;
		progress = newProgress;
		if (progressedAction == null) {
			getGui().flagAndUpdate(this);
		} else {
			getGui().flagAndAtomicUpdate(this, () -> progressedAction.accept(this, oldProgress, progress, player));
		}
	}
}
