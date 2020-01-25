package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.DiscreteSliderGuiElement;
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
 * The implementation of {@link DiscreteSliderGuiElement}.
 */
public class DiscreteSliderGuiElementImpl extends GuiElementImpl<DiscreteSliderGuiElement> implements DiscreteSliderGuiElement {
	private short width = 150;
	private boolean editable = true;
	private String tooltip = "";
	private String text = "";
	private short minProgress;
	private short maxProgress = 100;
	private short progress;
	private ProgressedAction progressedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public DiscreteSliderGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.DISCRETE_SLIDER, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Contract(pure = true)
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getTooltip() {
		return tooltip;
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
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getRoot().flagAndUpdate(this);
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
		getRoot().flagAndUpdate(this);
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
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public DiscreteSliderGuiElementImpl setProgress(int progress) {
		Validate.isTrue(progress >= 0 && progress <= maxProgress,
				"Progress must be at least 0 and at most maxProgress");
		this.progress = (short) progress;
		getRoot().flagAndUpdate(this);
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
		buffer.putShort(width);
		buffer.putBool(editable);
		buffer.putString(tooltip);
		buffer.putString(text);
		buffer.putShort((short) (maxProgress - minProgress));
		buffer.putShort((short) (progress - minProgress));
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
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
			getRoot().flagAndUpdate(this);
		} else {
			getRoot().flagAndAtomicUpdate(this, () -> progressedAction.accept(this, oldProgress, progress, player));
		}
	}
}
