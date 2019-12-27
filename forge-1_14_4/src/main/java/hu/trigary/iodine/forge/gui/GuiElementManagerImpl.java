package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.GuiElementManager;
import hu.trigary.iodine.client.gui.container.GridGuiContainer;
import hu.trigary.iodine.client.gui.container.LinearGuiContainer;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
			case CHECKBOX:
			case DROPDOWN:
			case IMAGE:
			case PROGRESS_BAR:
			case RADIO_BUTTON:
			case SLIDER:
			case TEXT_FIELD:
			case TEXT:
				throw new NotImplementedException(); //TODO implement them
			default:
				throw new AssertionError("Invalid GUiElementType: " + type);
		}
	}
}
