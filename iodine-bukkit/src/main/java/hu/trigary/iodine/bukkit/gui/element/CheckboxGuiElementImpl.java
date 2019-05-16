package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.CheckboxGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link CheckboxGuiElement}.
 */
public class CheckboxGuiElementImpl extends GuiElementImpl<CheckboxGuiElement> implements CheckboxGuiElement {
	private boolean editable = true;
	private boolean checked;
	private ClickedAction<CheckboxGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public CheckboxGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
	}
	
	
	
	@Contract(pure = true)
	@Override
	public boolean isEditable() {
		return editable;
	}
	
	@Contract(pure = true)
	@Override
	public boolean isChecked() {
		return checked;
	}
	
	
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setEditable(boolean editable) {
		this.editable = editable;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl setChecked(boolean checked) {
		this.checked = checked;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public CheckboxGuiElementImpl onClicked(@Nullable ClickedAction<CheckboxGuiElement> action) {
		clickedAction = action;
		return this;
	}
}
