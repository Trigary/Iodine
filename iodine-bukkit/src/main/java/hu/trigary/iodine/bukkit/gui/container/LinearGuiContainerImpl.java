package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.LinearGuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.GuiElementImpl;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link LinearGuiContainer}.
 */
public class LinearGuiContainerImpl extends GuiElementImpl<LinearGuiContainer>
		implements LinearGuiContainer, GuiParentPlus<LinearGuiContainer> {
	private final List<GuiElementImpl<?>> children = new ArrayList<>();
	private boolean verticalOrientation;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public LinearGuiContainerImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.CONTAINER_LINEAR, internalId, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public List<GuiElement<?>> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	@Contract(pure = true)
	@Override
	public boolean isVerticalOrientation() {
		return verticalOrientation;
	}
	
	
	
	
	@NotNull
	@Override
	public LinearGuiContainer setOrientation(boolean vertical) {
		verticalOrientation = vertical;
		gui.update();
		return this;
	}
	
	
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChildLast(@NotNull E element) {
		makeChildAt(children.size(), element);
		return element;
	}
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChildFirst(@NotNull E element) {
		makeChildAt(0, element);
		return element;
	}
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChildAfter(@NotNull E element, @NotNull GuiElement<?> after) {
		//noinspection SuspiciousMethodCalls
		int index = children.indexOf(after);
		Validate.isTrue(index != -1, "The specified element is not a child of this parent");
		makeChildAt(index + 1, element);
		return element;
	}
	
	@NotNull
	@Override
	public <E extends GuiElement<E>> E makeChildBefore(@NotNull E element, @NotNull GuiElement<?> before) {
		//noinspection SuspiciousMethodCalls
		int index = children.indexOf(before);
		Validate.isTrue(index != -1, "The specified element is not a child of this parent");
		makeChildAt(index, element);
		return element;
	}
	
	
	
	private void makeChildAt(int index, GuiElement<?> element) {
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		children.add(index, impl);
		impl.setParent(this);
		gui.update();
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		Validate.isTrue(children.remove(child),
				"The specified element is not a child of this parent");
	}
	
	
	
	@Override
	public void serialize(@NotNull ByteBuffer buffer) {
		super.serialize(buffer);
		serializeBoolean(buffer, verticalOrientation);
		buffer.putInt(children.size());
		for (GuiElementImpl<?> element : children) {
			buffer.putInt(element.getInternalId());
		}
	}
}
