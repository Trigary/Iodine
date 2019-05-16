package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ProgressBarGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ProgressBarGuiElement}.
 */
public class ProgressBarGuiElementImpl extends GuiElementImpl<ProgressBarGuiElement> implements ProgressBarGuiElement {
	private boolean verticalOrientation;
	private String text;
	private int maxProgress;
	private int progress;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ProgressBarGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
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
	public ProgressBarGuiElementImpl setOrientation(boolean vertical) {
		verticalOrientation = vertical;
		gui.update();
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
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setProgress(int progress) {
		Validate.isTrue(progress >= 0 && progress <= maxProgress,
				"Progress must be at least 0 and most maxProgress");
		this.progress = progress;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ProgressBarGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		gui.update();
		return this;
	}
}
