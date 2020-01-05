package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.gui.ElementManager;
import hu.trigary.iodine.client.gui.container.GridGuiContainer;
import hu.trigary.iodine.client.gui.container.LinearGuiContainer;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.forge.gui.element.*;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ElementManager}.
 */
public class ElementManagerImpl extends ElementManager {
	@NotNull
	@Override
	protected ElementConstructor<?> getElementConstructor(@NotNull GuiElementType type) {
		switch (type) {
			case CONTAINER_ROOT:
				return RootGuiContainer::new;
			case CONTAINER_GRID:
				return GridGuiContainer::new;
			case CONTAINER_LINEAR:
				return LinearGuiContainer::new;
			case BUTTON:
				return ButtonGuiElementImpl::new;
			case CHECKBOX:
				return CheckboxGuiElementImpl::new;
			case CONTINUOUS_SLIDER:
				return ContinuousGuiElementImpl::new;
			case DISCRETE_SLIDER:
				return DiscreteSliderGuiElementImpl::new;
			case DROPDOWN:
				return DropdownGuiElementImpl::new;
			case IMAGE:
				return ImageGuiElementImpl::new;
			case PROGRESS_BAR:
				return ProgressBarGuiElementImpl::new;
			case RADIO_BUTTON:
				return RadioButtonGuiElementImpl::new;
			case RECTANGLE:
				return RectangleGuiElementImpl::new;
			case TEXT_FIELD:
				return TextFieldGuiElementImpl::new;
			case TEXT:
				return TextGuiElementImpl::new;
			default:
				throw new AssertionError("Invalid GuiElementType: " + type);
		}
	}
}
