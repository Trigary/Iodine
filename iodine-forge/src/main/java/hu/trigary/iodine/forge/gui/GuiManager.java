package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.container.RootGuiContainer;
import hu.trigary.iodine.forge.gui.element.*;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;

public class GuiManager {
	private final Map<GuiElementType, ElementConstructor<?>> constructors = new EnumMap<>(GuiElementType.class);
	private final IodineMod mod;
	
	public GuiManager(@NotNull IodineMod mod) {
		this.mod = mod;
		
		constructors.put(GuiElementType.CONTAINER_ROOT, RootGuiContainer::new);
		constructors.put(GuiElementType.BUTTON, ButtonGuiElement::new);
		constructors.put(GuiElementType.CHECKBOX, CheckboxGuiElement::new);
		constructors.put(GuiElementType.DROPDOWN, DropdownGuiElement::new);
		constructors.put(GuiElementType.IMAGE, ImageGuiElement::new);
		constructors.put(GuiElementType.PROGRESS_BAR, ProgressBarGuiElement::new);
		constructors.put(GuiElementType.RADIO_BUTTON, RadioButtonGuiElement::new);
		constructors.put(GuiElementType.SLIDER, SliderGuiElement::new);
		constructors.put(GuiElementType.TEXT_FIELD, TextFieldGuiElement::new);
		constructors.put(GuiElementType.TEXT, TextGuiElement::new);
	}
	
	
	
	public void deserializeElement(@NotNull IodineGui gui,
			@NotNull Map<Integer, GuiElement> storage, @NotNull ByteBuffer buffer) {
		GuiElementType type = GuiElementType.fromId(buffer.get());
		if (type == null) {
			mod.getLogger().error("Encountered invalid GuiElementType while deserializing");
			return;
		}
		
		int id = buffer.getInt();
		GuiElement element = storage.computeIfAbsent(id, ignored -> constructors.get(type).apply(gui, id));
		element.deserialize(buffer);
	}
	
	
	
	@FunctionalInterface
	private interface ElementConstructor<R extends GuiElement> {
		R apply(IodineGui gui, int id);
	}
}
