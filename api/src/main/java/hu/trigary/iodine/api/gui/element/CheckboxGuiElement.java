package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that can be checked and unchecked.
 */
public interface CheckboxGuiElement extends GuiElement<CheckboxGuiElement>,
		GuiEditable<CheckboxGuiElement>, GuiTooltipable<CheckboxGuiElement>,
		GuiClickable<CheckboxGuiElement> {
	/**
	 * Gets whether this element is currently in its checked state.
	 *
	 * @return whether this element is checked
	 */
	@Contract(pure = true)
	boolean isChecked();
	
	/**
	 * Sets this element's checked state.
	 *
	 * @param checked the new checked state
	 * @return the current instance (for chaining)
	 */
	@NotNull
	CheckboxGuiElement setChecked(boolean checked);
}
