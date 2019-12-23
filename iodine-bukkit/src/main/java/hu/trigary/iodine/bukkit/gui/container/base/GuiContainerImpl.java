package hu.trigary.iodine.bukkit.gui.container.base;

import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
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
	 * @param gui the GUI which will contain this element
	 * @param type the exact type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	protected GuiContainerImpl(@NotNull GuiBaseImpl<?> gui, @NotNull GuiElementType type, int internalId, @NotNull Object id) {
		super(gui, type, internalId, id);
	}
	
	
	
	@Override
	public void setDrawPriority(byte priority) {
		super.setDrawPriority(priority);
		for (GuiElement<?> child : getChildren()) {
			if (child.getDrawPriority() < priority) {
				child.setDrawPriority(priority);
			}
		}
	}
}
