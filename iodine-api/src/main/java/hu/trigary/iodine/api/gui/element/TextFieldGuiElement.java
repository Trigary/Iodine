package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a text input field.
 */
public interface TextFieldGuiElement extends GuiElement<TextFieldGuiElement>,
		GuiWidthSettable<TextFieldGuiElement>, GuiHeightSettable<TextFieldGuiElement>,
		GuiTextable<TextFieldGuiElement>, GuiEditable<TextFieldGuiElement> {
	/**
	 * Gets the regex used to determine whether the user input should be accepted client-side.
	 * An empty {@link String} stands for no validation.
	 * The default value is an empty {@link String}.
	 *
	 * @return the validator regex or an empty {@link String}, if there is none
	 */
	@NotNull
	String getRegex();
	
	
	
	/**
	 * Sets the regex used to determine whether the user input should be accepted client-side.
	 * An empty {@link String} stands for no validation.
	 *
	 * @param regex the new regex to use
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextFieldGuiElement setRegex(@NotNull String regex);
	
	/**
	 * Sets the action that should be executed when
	 * this GUI element's text is changed by a player.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
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
		void accept(@NotNull TextFieldGuiElement element, @NotNull String oldText,
				@NotNull String newText, @NotNull Player player);
	}
}
