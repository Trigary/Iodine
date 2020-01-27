package hu.trigary.iodine.client.gui.container.base;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

/**
 * The base class for GUI elements that can be the parent of other elements.
 */
public abstract class GuiContainer extends GuiElement {
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected GuiContainer(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	public abstract void initialize();
	
	/**
	 * Converts number-based IDs to object references.
	 *
	 * @param childrenIdentifiers the number-based IDs to convert
	 * @return the resolved elements
	 */
	@NotNull
	protected final GuiElement[] resolveChildren(@NotNull int[] childrenIdentifiers) {
		GuiElement[] children = new GuiElement[childrenIdentifiers.length];
		for (int i = 0; i < children.length; i++) {
			int id = childrenIdentifiers[i];
			children[i] = id == getId() ? null : getRoot().getElement(childrenIdentifiers[i]);
		}
		return children;
	}
	
	@Override
	protected abstract void setChildrenPositions(int offsetX, int offsetY);
	
	@Override
	protected void updateImpl(int positionX, int positionY, int width, int height) {}
	
	@Override
	protected void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks) {}
}
