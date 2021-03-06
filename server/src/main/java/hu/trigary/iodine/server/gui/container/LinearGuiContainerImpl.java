package hu.trigary.iodine.server.gui.container;

import hu.trigary.iodine.api.gui.container.LinearGuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.container.base.GuiContainerImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link LinearGuiContainer}.
 */
public class LinearGuiContainerImpl extends GuiContainerImpl<LinearGuiContainer> implements LinearGuiContainer {
	private final List<GuiElementImpl<?>> children = new ArrayList<>();
	private boolean verticalOrientation;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public LinearGuiContainerImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.CONTAINER_LINEAR, internalId, id);
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
		getRoot().flagAndUpdate(this);
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
	
	
	
	private void makeChildAt(int index, @NotNull GuiElement<?> element) {
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		if (impl.getParent() != this || children.indexOf(element) != index) {
			impl.setParent(this);
			children.add(index, impl);
			getRoot().flagAndUpdate(this);
		}
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		Validate.isTrue(children.remove(child),
				"The specified element is not a child of this parent");
		getRoot().flagOnly(this);
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putBool(verticalOrientation);
		buffer.putShort((short) children.size());
		for (GuiElementImpl<?> element : children) {
			buffer.putInt(element.getInternalId());
		}
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {}
}
