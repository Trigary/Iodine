package hu.trigary.iodine.api.gui.container;

import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiOrientable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

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
	LinearGuiContainer makeChildLast(@NotNull GuiElement<?> element);
	
	/**
	 * Makes the specified element a direct child of this container.
	 * The element is inserted before the other children.
	 *
	 * @param element the element to add as as child
	 * @return the current instance (for chaining)
	 */
	@NotNull
	LinearGuiContainer makeChildFirst(@NotNull GuiElement<?> element);
	
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
	LinearGuiContainer makeChildAfter(@NotNull GuiElement<?> element, @NotNull GuiElement<?> after);
	
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
	LinearGuiContainer makeChildBefore(@NotNull GuiElement<?> element, @NotNull GuiElement<?> after);
	
	
	
	/**
	 * Adds a new element of the specified type to this container.
	 * The parent of the element will be this class instance.
	 * The initializer function atomically initializes the new element.
	 * The element will be accessible using the specified ID.
	 * This ID must be unique.
	 * If ID-based access is not required, then optionally
	 * {@link #addElement(GuiElements, Consumer)} can be used instead.
	 *
	 * @param id id the identifier of the element
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	<E extends GuiElement<E>> IodineGui addElement(@NotNull Object id,
			@NotNull GuiElements<E> type, @NotNull Consumer<E> initializer);
	
	/**
	 * Adds a new element of the specified type to this container.
	 * The parent of the element will be this class instance.
	 * The initializer function atomically initializes the new element.
	 * The element will have an internally created ID.
	 * If ID-based access is required, then {@link #addElement(Object, GuiElements, Consumer)}
	 * should be used instead.
	 *
	 * @param type the class of the element to add
	 * @param initializer the function which atomically initializes the element
	 * @param <E> the type of the element to add
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default <E extends GuiElement<E>> IodineGui addElement(@NotNull GuiElements<E> type,
			@NotNull Consumer<E> initializer) {
		return addElement(new Object(), type, initializer);
	}
}
