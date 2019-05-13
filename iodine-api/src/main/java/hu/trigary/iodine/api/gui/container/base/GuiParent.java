package hu.trigary.iodine.api.gui.container.base;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a {@link IodineGui} or a {@link GuiContainer}.
 * Instances of this class can be the parent of {@link GuiElement}s.
 * The parent of an element determines where the element is rendered.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiParent<T extends GuiParent<T>> {
	/**
	 * Gets the elements that have this instance as their parent.
	 * This operation is not recursive, children of the children are not included.
	 * The returned collection is an unmodifiable view of the underlying data.
	 *
	 * @return the elements that have this instance as their parent
	 */
	@NotNull
	@Contract(pure = true)
	Collection<GuiElement<?>> getChildren();
}
