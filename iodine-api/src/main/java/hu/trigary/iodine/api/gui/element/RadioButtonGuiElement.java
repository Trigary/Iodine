package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiCheckable;
import hu.trigary.iodine.api.gui.element.base.GuiEditable;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a radio button.
 * Radio buttons are linked together by their common group-ID, which is '0' by default.
 */
public interface RadioButtonGuiElement extends GuiElement<RadioButtonGuiElement>,
		GuiCheckable<RadioButtonGuiElement>, GuiEditable<RadioButtonGuiElement> {
	/**
	 * Sets this element's group-ID to the specified ID.
	 * If this element is the first one with the specified ID,
	 * then this element will become checked.
	 * Otherwise this element will become unchecked.
	 *
	 * @param groupId the new group-ID of this element
	 * @return the current instance (for chaining)
	 */
	@NotNull
	RadioButtonGuiElement setGroupId(int groupId);
	
	
	
	/**
	 * Sets the action that should be executed when
	 * this GUI element is unchecked by a player.
	 * This callback fires before {@link #onChecked(CheckedAction)}.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	RadioButtonGuiElement onUnchecked(@Nullable UncheckedAction action);
	/**
	 * Sets the action that should be executed when
	 * this GUI element is checked by a player.
	 * This callback fires after {@link #onUnchecked(UncheckedAction)}.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	RadioButtonGuiElement onChecked(@Nullable CheckedAction action);
	
	
	
	/**
	 * The handler of the unchecked action.
	 */
	@FunctionalInterface
	interface UncheckedAction {
		/**
		 * Handles the unchecked action.
		 *
		 * @param checked the element that became checked
		 * @param unchecked the element that became unchecked
		 * @param player the player who caused the change
		 */
		void apply(@NotNull RadioButtonGuiElement checked,
				@NotNull RadioButtonGuiElement unchecked, @NotNull Player player);
	}
	
	/**
	 * The handler of the checked action.
	 */
	@FunctionalInterface
	interface CheckedAction {
		/**
		 * Handles the checked action.
		 *
		 * @param checked the element that became checked
		 * @param unchecked the element that became unchecked
		 * @param player the player who caused the change
		 */
		void apply(@NotNull RadioButtonGuiElement checked,
				@NotNull RadioButtonGuiElement unchecked, @NotNull Player player);
	}
}
