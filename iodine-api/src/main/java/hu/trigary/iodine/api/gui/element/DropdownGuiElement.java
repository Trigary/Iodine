package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A GUI element that allows viewers to select a single value from a list of choices.
 */
public interface DropdownGuiElement extends GuiElement<DropdownGuiElement>, GuiEditable<DropdownGuiElement> {
	/**
	 * Gets the values that can be selected.
	 *
	 * @return the selectable values
	 */
	@NotNull
	@Contract(pure = true)
	List<String> getChoices();
	
	/**
	 * Gets the value that is currently selected.
	 *
	 * @return the currently selected value
	 */
	@NotNull
	@Contract(pure = true)
	String getSelected();
	
	
	
	/**
	 * Sets the values that should be selectable.
	 * Clears all previous choices.
	 * The count of elements must be at least one.
	 * The first element will become selected.
	 *
	 * @param choices the new selectable values
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DropdownGuiElement setChoices(@NotNull Collection<String> choices);
	
	/**
	 * Sets the values that should be selectable.
	 * Clears all previous choices.
	 * The count of elements must be at least one.
	 * The first element will become selected.
	 *
	 * @param choices the new selectable values
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default DropdownGuiElement setChoices(@NotNull String... choices) {
		return setChoices(Arrays.asList(choices));
	}
	
	/**
	 * Sets the value that should be selected.
	 * An exception is thrown if this value is not contained in {@link #getChoices()}.
	 *
	 * @param value the value that should become selected
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DropdownGuiElement setSelected(@NotNull String value);
	
	/**
	 * Sets the action that should be executed when
	 * this GUI element's chosen value is changed by a player.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	DropdownGuiElement onChosen(@Nullable ChosenAction action);
	
	
	
	/**
	 * The handler of the chosen action.
	 */
	@FunctionalInterface
	interface ChosenAction {
		/**
		 * Handles the chosen action.
		 *
		 * @param element the element whose chosen element changed
		 * @param oldChoice the selected value before this change
		 * @param newChoice the value selected by the player
		 * @param player the player who caused this change
		 */
		void accept(@NotNull DropdownGuiElement element, @NotNull String oldChoice,
				@NotNull String newChoice, @NotNull Player player);
	}
}
