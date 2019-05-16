package hu.trigary.iodine.api.gui.container;

import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiOrientable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A container that arranges its children one after another vertically or horizontally.
 */
public interface LinearGuiContainer extends GuiContainer<LinearGuiContainer>, GuiOrientable<LinearGuiContainer> {
	@NotNull
	@Contract(pure = true)
	@Override
	List<GuiElement<?>> getChildren();
	
	
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted after the other children.
	 *
	 * @param element the element to add as as child
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChildLast(@NotNull E element);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted before the other children.
	 *
	 * @param element the element to add as as child
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChildFirst(@NotNull E element);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted after the specified child.
	 * An exception is thrown if the specified element is not a child of this container.
	 *
	 * @param element the element to add as as child
	 * @param after the child after which this element should be inserted
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChildAfter(@NotNull E element, @NotNull GuiElement<?> after);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted before the specified child.
	 * An exception is thrown if the specified element is not a child of this container.
	 *
	 * @param element the element to add as as child
	 * @param after the child before which this element should be inserted
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> E makeChildBefore(@NotNull E element, @NotNull GuiElement<?> after);
}
