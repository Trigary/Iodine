package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends GuiElementImpl<ButtonGuiElement> implements ButtonGuiElement {
	private String text;
	private ClickedAction<ButtonGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ButtonGuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	
	
	@NotNull
	@Override
	public ButtonGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		gui.update();
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElementImpl onClicked(@Nullable ClickedAction<ButtonGuiElement> action) {
		clickedAction = action;
		return this;
	}
}