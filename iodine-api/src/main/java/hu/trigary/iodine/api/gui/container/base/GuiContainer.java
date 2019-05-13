package hu.trigary.iodine.api.gui.container.base;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.base.GuiElement;

/**
 * Represents a {@link GuiElement} that can contain other elements.
 * Primarily used for formatting purposes.
 * A notable {@link GuiParent} that is not a subclass of this class is {@link IodineGui}.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiContainer<T extends GuiContainer<T>> extends GuiElement<T>, GuiParent<T> {
}
