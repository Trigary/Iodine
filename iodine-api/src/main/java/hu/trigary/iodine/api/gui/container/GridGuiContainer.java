package hu.trigary.iodine.api.gui.container;

import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A container that arranges its children in columns and rows.
 */
public interface GridGuiContainer extends GuiContainer<GridGuiContainer> {
	/**
	 * Gets the element at the specified column-row pair.
	 * Returns null if there isn't an element at the specified position.
	 *
	 * @param column the column the element is contained in
	 * @param row the row the element is contained in
	 * @return the matching element or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	GuiElement<?> getChild(int column, int row);
	
	@NotNull
	GridGuiContainer setGridSize(int columns, int rows);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted into the specified column-row pair.
	 * An exception is thrown if that position already contains an element.
	 *
	 * @param element the element to add as as child
	 * @param column the column the element should be inserted into
	 * @param row the row the element should be inserted into
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChild(@NotNull E element, int column, int row);
}
