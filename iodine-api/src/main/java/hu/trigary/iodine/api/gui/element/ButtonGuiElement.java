package hu.trigary.iodine.api.gui.element;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * A GUI element that displays text and can be clicked.
 */
public interface ButtonGuiElement extends TextGuiElement {
	/**
	 * Sets the action that should be executed when this GUI element is clicked.
	 *
	 * @param action the action to execute
	 * @return the current instance (for chaining)
	 */
	@NotNull
	ButtonGuiElement setClickAction(@Nullable BiConsumer<? super ButtonGuiElement, Player> action);
	
	@NotNull
	@Override
	ButtonGuiElement setText(@NotNull String text);
}
