package hu.trigary.iodine.server.gui.container;

import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.container.base.GuiContainerImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * The implementation of {@link GridGuiContainer}.
 */
public class GridGuiContainerImpl extends GuiContainerImpl<GridGuiContainer> implements GridGuiContainer {
	private final List<GuiElementImpl<?>> children = new ArrayList<>();
	private short columnCount;
	private short rowCount;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public GridGuiContainerImpl(@NotNull IodineRootImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.CONTAINER_GRID, internalId, id);
		setGridSize(0, 0);
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
		validateGrid(columnCount, columns);
		validateGrid(rowCount, rows);
		
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
		
		columnCount = (short) columns;
		rowCount = (short) rows;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int column, int row) {
		Validate.isTrue(column >= 0 && row >= 0 && column < columnCount && row < rowCount,
				"The columns and rows must be at least 0 and less than the values specified in #setGridSize");
		Validate.isTrue(children.get(column * rowCount + row) == null, "That position is already taken by another child");
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		children.set(column * rowCount + row, impl);
		impl.setParent(this);
		getRoot().flagAndUpdate(this);
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		int index = children.indexOf(child);
		Validate.isTrue(index != -1, "The specified element is not a child of this parent");
		children.set(index, null);
		getRoot().flagOnly(this);
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort(columnCount);
		buffer.putShort(rowCount);
		for (GuiElementImpl<?> element : children) {
			buffer.putInt(element == null ? getInternalId() : element.getInternalId());
		}
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {}
	

	
	private void validateGrid(int currentCount, int newCount) {
		if (currentCount > newCount) {
			for (int row = newCount; row < currentCount; row++) {
				for (int column = 0; column < columnCount; column++) {
					Validate.isTrue(children.get(column * currentCount + row) == null,
							"Each child must fit into the new grid");
				}
			}
		}
	}
}
