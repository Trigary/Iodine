package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiTextable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a text input field.
 */
public interface TextFieldGuiElement extends GuiElement<TextFieldGuiElement>,
		GuiTextable<TextFieldGuiElement>, GuiEditable<TextFieldGuiElement> {
	/**
	 * Sets the action that should be executed when
	 * this GUI element's text is changed by a player.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextFieldGuiElement onChanged(@Nullable TextChangedAction action);
	
	
	
	/**
	 * The handler of the text changed action.
	 */
	@FunctionalInterface
	interface TextChangedAction {
		/**
		 * Handles the text changed action.
		 *
		 * @param element the element that was changed
		 * @param oldText the text before this change
		 * @param newText the text that was set by the player
		 * @param player the player who caused this change to happen
		 */
		void apply(@NotNull TextFieldGuiElement element, @NotNull String oldText,
				@NotNull String newText, @NotNull Player player);
	}
}
