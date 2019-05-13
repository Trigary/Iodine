package hu.trigary.iodine.api.gui.element.base;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link GuiElement} that can be clicked.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiClickable<T extends GuiClickable<T>> {
	/**
	 * Sets the action that should be executed when this GUI element is clicked.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T onClicked(@Nullable ClickedAction<T> action);
	
	
	
	/**
	 * The handler of the clicked action.
	 *
	 * @param <T> the class implementing the {@link GuiClickable} interface
	 */
	@FunctionalInterface
	interface ClickedAction<T extends GuiClickable<T>> {
		/**
		 * Handles the clicked action.
		 *
		 * @param element the element that was clicked
		 * @param player the player who clicked the element
		 */
		void accept(@NotNull GuiClickable<T> element, @NotNull Player player);
	}
}
