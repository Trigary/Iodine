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
	 * Indexing starts at 0.
	 *
	 * @param column the column the element is contained in
	 * @param row the row the element is contained in
	 * @return the matching element or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	GuiElement<?> getChild(int column, int row);
	
	/**
	 * Gets the amount of columns set by {@link #setGridSize(int, int)}.
	 *
	 * @return the amount of columns
	 */
	@Contract(pure = true)
	int getColumnCount();
	
	/**
	 * Gets the amount of rows set by {@link #setGridSize(int, int)}.
	 *
	 * @return the amount of rows
	 */
	@Contract(pure = true)
	int getRowCount();
	
	/**
	 * Sets the dimensions of this grid.
	 * Each child must fit into the new grid.
	 *
	 * @param columns the count of columns
	 * @param rows the count of rows
	 * @return the current instance (for chaining)
	 */
	@NotNull
	GridGuiContainer setGridSize(int columns, int rows);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * If the element is already a child it is moved to the new position if necessary.
	 * The element is inserted into the specified column-row pair.
	 * An exception is thrown if that position already contains an element.
	 * Indexing starts at 0.
	 *
	 * @param element the element to add as as child
	 * @param column the column the element should be inserted into
	 * @param row the row the element should be inserted into
	 * @param <E> the type of the element
	 * @return the new child (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChild(@NotNull E element, int column, int row);
}
