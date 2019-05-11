package hu.trigary.iodine.bukkit.api.gui.element;

import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * The implementation of {@link ButtonGuiElement}.
 */
public class ButtonGuiElementImpl extends TextGuiElementImpl implements ButtonGuiElement {
	private BiConsumer<? super ButtonGuiElement, Player> clickAction;
	
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
	@Override
	public ButtonGuiElement setClickAction(@Nullable BiConsumer<? super ButtonGuiElement, Player> action) {
		clickAction = action;
		return this;
	}
	
	@NotNull
	@Override
	public ButtonGuiElement setText(@NotNull String text) {
		super.setText(text);
		return this;
	}
}
