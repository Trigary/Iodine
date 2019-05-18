package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.base.GuiParent;
import hu.trigary.iodine.bukkit.gui.element.GuiElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link GuiParent}.
 * All implementations of {@link GuiParent} must also implement this interface.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiParentPlus<T extends GuiParent<T>> extends GuiParent<T> {
	/**
	 * Removes the specified child from this instance.
	 * Should only be called by {@link GuiElementImpl}.
	 *
	 * @param child the child to remove
	 */
	void removeChild(@NotNull GuiElementImpl<?> child);
}
