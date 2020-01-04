package hu.trigary.iodine.server.gui.container.base;

import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link GuiContainer}.
 *
 * @param <T> the class implementing this abstract class
 */
public abstract class GuiContainerImpl<T extends GuiContainer<T>> extends GuiElementImpl<T>
		implements GuiContainer<T>, GuiParentPlus<T> {
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param type the exact type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	protected GuiContainerImpl(@NotNull IodineRootImpl<?> root, @NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(root, type, internalId, id);
	}
	
	
	
	@NotNull
	@Override
	public final T setDrawPriority(int priority) {
		for (GuiElement<?> child : getChildren()) {
			if (child.getDrawPriority() < priority) {
				child.setDrawPriority(priority);
			}
		}
		return super.setDrawPriority(priority);
	}
}
