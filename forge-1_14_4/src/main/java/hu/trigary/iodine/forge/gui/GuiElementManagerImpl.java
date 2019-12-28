package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.GuiElementManager;
import hu.trigary.iodine.client.gui.container.GridGuiContainer;
import hu.trigary.iodine.client.gui.container.LinearGuiContainer;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.forge.gui.element.*;
import org.jetbrains.annotations.NotNull;

public class GuiElementManagerImpl extends GuiElementManager {
	public GuiElementManagerImpl(@NotNull IodineMod mod) {
		super(mod);
	}
	
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
			case DROPDOWN:
				return DropdownGuiElementImpl::new;
			case IMAGE:
				return ImageGuiElementImpl::new;
			case PROGRESS_BAR:
				return ProgressBarGuiElementImpl::new;
			case RADIO_BUTTON:
				return RadioButtonGuiElementImpl::new;
			case SLIDER:
				return SliderGuiElementImpl::new;
			case TEXT_FIELD:
				return TextFieldGuiElementImpl::new;
			case TEXT:
				return TextGuiElementImpl::new;
			default:
				throw new AssertionError("Invalid GUiElementType: " + type);
		}
	}
}
