package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.GuiElementImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GridGuiContainerImpl extends GuiElementImpl<GridGuiContainer>
		implements GridGuiContainer, GuiParentPlus<GridGuiContainer> {
	private final List<GuiElementImpl<?>> children = new ArrayList<>();
	private int columnCount;
	private int rowCount;
	
	public GridGuiContainerImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
		setGridSize(0, 0);
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	@Override
	public GuiElement<?> getChild(int column, int row) {
		//TODO validate
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
		columnCount = columns;
		rowCount = rows;
		//TODO copy them over, but if they don't fit?
		// -> validate that all elements can fit into the new size
		//TODO add/remove elements so the size equals columns * rows
		gui.update();
		return this;
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int column, int row) {
		//TODO validate
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		children.set(column * rowCount + row, impl);
		impl.setParent(this);
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		int index = children.indexOf(child);
		Validate.isTrue(index != -1, "The specified element is not a child of this parent");
		children.set(index, null);
	}
}
