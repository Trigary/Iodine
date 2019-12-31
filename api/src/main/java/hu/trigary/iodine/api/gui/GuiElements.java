package hu.trigary.iodine.api.gui;

import hu.trigary.iodine.api.gui.container.GridGuiContainer;
import hu.trigary.iodine.api.gui.container.LinearGuiContainer;
import hu.trigary.iodine.api.gui.element.*;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GuiElement}s that can be added to GUIs.
 *
 * @param <T> the type of the element
 */
public final class GuiElements<T extends GuiElement<T>> {
	public static final GuiElements<GridGuiContainer> CONTAINER_GRID = new GuiElements<>(GridGuiContainer.class);
	public static final GuiElements<LinearGuiContainer> CONTAINER_LINEAR = new GuiElements<>(LinearGuiContainer.class);
	public static final GuiElements<ButtonGuiElement> BUTTON = new GuiElements<>(ButtonGuiElement.class);
	public static final GuiElements<CheckboxGuiElement> CHECKBOX = new GuiElements<>(CheckboxGuiElement.class);
	public static final GuiElements<ContinuousSliderGuiElement> CONTINUOUS_SLIDER = new GuiElements<>(ContinuousSliderGuiElement.class);
	public static final GuiElements<DiscreteSliderGuiElement> DISCRETE_SLIDER = new GuiElements<>(DiscreteSliderGuiElement.class);
	public static final GuiElements<DropdownGuiElement> DROPDOWN = new GuiElements<>(DropdownGuiElement.class);
	public static final GuiElements<ImageGuiElement> IMAGE = new GuiElements<>(ImageGuiElement.class);
	public static final GuiElements<ProgressBarGuiElement> PROGRESS_BAR = new GuiElements<>(ProgressBarGuiElement.class);
	public static final GuiElements<RadioButtonGuiElement> RADIO_BUTTON = new GuiElements<>(RadioButtonGuiElement.class);
	public static final GuiElements<RectangleGuiElement> RECTANGLE = new GuiElements<>(RectangleGuiElement.class);
	public static final GuiElements<TextFieldGuiElement> TEXT_FIELD = new GuiElements<>(TextFieldGuiElement.class);
	public static final GuiElements<TextGuiElement> TEXT = new GuiElements<>(TextGuiElement.class);
	
	private final Class<T> type;
	
	private GuiElements(Class<T> type) {
		this.type = type;
	}
	
	/**
	 * Gets the class that represents the {@link GuiElement}
	 * associated with this enum value.
	 *
	 * @return the class of this element
	 */
	@NotNull
	@Contract(pure = true)
	public Class<T> getType() {
		return type;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return type.getSimpleName();
	}
}
