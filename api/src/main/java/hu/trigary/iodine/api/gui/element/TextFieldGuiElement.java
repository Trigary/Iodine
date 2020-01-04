package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a text input field.
 */
public interface TextFieldGuiElement extends GuiElement<TextFieldGuiElement>,
		GuiWidthSettable<TextFieldGuiElement>, GuiHeightSettable<TextFieldGuiElement>,
		GuiTextable<TextFieldGuiElement>, GuiEditable<TextFieldGuiElement> {
	/**
	 * Gets the regex used to determine whether the text should be accepted.
	 * An empty {@link String} stands for no validation.
	 * The default value is an empty {@link String}.
	 *
	 * @return the validator regex or an empty {@link String}, if there is none
	 */
	@NotNull
	@Contract(pure = true)
	String getRegex();
	
	/**
	 * Gets the maximum amount of characters the user can have in this text field.
	 * The default value is 32.
	 *
	 * @return the maximum length of the text when it comes to user input
	 */
	@Contract(pure = true)
	int getMaxLength();
	
	
	
	/**
	 * Sets the regex used to determine whether the text should be accepted.
	 * An empty {@link String} stands for no validation.
	 * The current text must match this regex.
	 *
	 * @param regex the new regex to use
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextFieldGuiElement setRegex(@NotNull String regex);
	
	/**
	 * Sets the maximum amount of characters the can be in this text field.
	 * The current text must not be longer.
	 * The value must be positive and at most 250.
	 * (The limit protects against malicious clients and helps obey possible client-side limits.)
	 *
	 * @param maxLength the new length limit to use
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextFieldGuiElement setMaxLength(int maxLength);
	
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
				@NotNull String newText, @NotNull IodinePlayer player);
	}
}
