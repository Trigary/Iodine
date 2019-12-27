package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI element that is a slider, with text on top of it.
 * Only the width or the height is customizable (not both) depending on the orientation.
 * The non-configurable dimension's getter returns 0.
 */
public interface SliderGuiElement extends GuiElement<SliderGuiElement>,
		GuiWidthSettable<SliderGuiElement>, GuiHeightSettable<SliderGuiElement>,
		GuiTextable<SliderGuiElement>, GuiProgressable<SliderGuiElement>,
		GuiOrientable<SliderGuiElement>, GuiEditable<SliderGuiElement> {
	/**
	 * Sets the action that should be executed when
	 * this GUI element's progress is changed by a player.
	 * The callback is atomically executed GUI updating wise.
	 *
	 * @param action the action to atomically execute
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
		void accept(@NotNull SliderGuiElement element, int oldProgress, int newProgress, @NotNull Player player);
	}
}