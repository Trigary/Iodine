package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a slider, with text on top of it.
 */
public interface SliderGuiElement extends GuiElement<SliderGuiElement>, GuiTextable<SliderGuiElement>,
		GuiProgressable<SliderGuiElement>, GuiOrientable<SliderGuiElement>, GuiEditable<SliderGuiElement> {
	/**
	 * Sets the action that should be executed when
	 * this GUI element's progress is changed by a player.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	SliderGuiElement onProgressed(@Nullable ProgressedAction action);
	
	
	
	/**
	 * The handler of the text changed action.
	 */
	@FunctionalInterface
	interface ProgressedAction {
		/**
		 * Handles the text changed action.
		 *
		 * @param element the element that progressed
		 * @param oldProgress the progress before this change
		 * @param newProgress the progress that was set by the player
		 * @param player the player who changed the progress
		 */
		void apply(@NotNull SliderGuiElement element, int oldProgress, int newProgress, @NotNull Player player);
	}
}
