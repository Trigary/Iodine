package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.SliderGuiElement;
import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link SliderGuiElement}.
 */
public class SliderGuiElementImpl extends GuiElementImpl<SliderGuiElement> implements SliderGuiElement {
	private int width = 150;
	private boolean editable = true;
	private boolean verticalOrientation;
	private String text = "";
	private int maxProgress;
	private int progress;
	private ProgressedAction progressedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public SliderGuiElementImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.SLIDER, internalId, id);
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
	public SliderGuiElementImpl setWidth(int width) {
		Validate.isTrue(width > 0 && width <= Short.MAX_VALUE, "The width must be positive and at most Short.MAX_VALUE");
		this.width = width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public SliderGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public SliderGuiElementImpl setOrientation(boolean vertical) {
		verticalOrientation = vertical;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public SliderGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public SliderGuiElementImpl setMaxProgress(int maxProgress) {
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
	public SliderGuiElementImpl setProgress(int progress) {
		Validate.isTrue(progress >= 0 && progress <= maxProgress,
				"Progress must be at least 0 and at most maxProgress");
		this.progress = progress;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public SliderGuiElementImpl onProgressed(@Nullable ProgressedAction action) {
		progressedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ByteBuffer buffer) {
		buffer.putShort((short) width);
		BufferUtils.serializeBoolean(buffer, editable);
		BufferUtils.serializeBoolean(buffer, verticalOrientation);
		BufferUtils.serializeString(buffer, text);
		buffer.putInt(maxProgress);
		buffer.putInt(progress);
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		throw new NotImplementedException();
	}
}
