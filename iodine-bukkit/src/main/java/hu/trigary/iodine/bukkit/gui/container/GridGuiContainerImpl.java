package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * The implementation of {@link GridGuiContainer}.
 */
public class GridGuiContainerImpl extends GuiElementImpl<GridGuiContainer>
		implements GridGuiContainer, GuiParentPlus<GridGuiContainer> {
	private final List<GuiElementImpl<?>> children = new ArrayList<>();
	private int columnCount;
	private int rowCount;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public GridGuiContainerImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.CONTAINER_GRID, internalId, id);
		setGridSize(0, 0);
		throw new NotImplementedException();
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	@Override
	public GuiElement<?> getChild(int column, int row) {
		Validate.isTrue(column >= 0 && row >= 0 && column < columnCount && row < rowCount,
				"The columns and rows must be at least 0 and less than the values specified in #setGridSize");
		return children.get(column * rowCount + row);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Collection<GuiElement<?>> getChildren() {
		return Collections.unmodifiableCollection(children);
	}
	
	
	
	@NotNull
	@Override
	public GridGuiContainerImpl setGridSize(int columns, int rows) {
		if (columnCount > columns) {
			for (int column = columns; column < columnCount; column++) {
				for (int row = 0; row < rowCount; row++) {
					Validate.isTrue(children.get(column * rowCount + row) == null,
							"Each child must fit into the new grid");
				}
			}
		}
		if (rowCount > rows) {
			for (int row = rows; row < rowCount; row++) {
				for (int column = 0; column < columnCount; column++) {
					Validate.isTrue(children.get(column * rowCount + row) == null,
							"Each child must fit into the new grid");
				}
			}
		}
		
		GuiElementImpl<?>[] old = children.toArray(new GuiElementImpl<?>[0]);
		children.clear();
		
		int newSize = columns * rows;
		while (children.size() < newSize) {
			children.add(null);
		}
		while (children.size() > newSize) {
			children.remove(children.size() - 1);
		}
		
		int columnMin = Math.min(columnCount, columns);
		int rowMin = Math.min(rowCount, rows);
		for (int column = 0; column < columnMin; column++) {
			for (int row = 0; row < rowMin; row++) {
				children.set(column * rows + row, old[column * rowCount + row]);
			}
		}
		
		columnCount = columns;
		rowCount = rows;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int column, int row) {
		Validate.isTrue(column >= 0 && row >= 0 && column < columnCount && row < rowCount,
				"The columns and rows must be at least 0 and less than the values specified in #setGridSize");
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		children.set(column * rowCount + row, impl);
		impl.setParent(this);
		getGui().flagAndUpdate(this);
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		int index = children.indexOf(child);
		Validate.isTrue(index != -1, "The specified element is not a child of this parent");
		children.set(index, null);
		getGui().flagOnly(this);
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ByteBuffer buffer) {
		buffer.putInt(columnCount);
		buffer.putInt(rowCount);
		for (GuiElementImpl<?> element : children) {
			buffer.putInt(element == null ? getInternalId() : element.getInternalId());
		}
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		throw new NotImplementedException();
	}
}
