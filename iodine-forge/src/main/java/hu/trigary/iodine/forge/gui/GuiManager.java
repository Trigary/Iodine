package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.element.ButtonGuiElement;
import hu.trigary.iodine.forge.gui.element.GuiElement;
import hu.trigary.iodine.forge.gui.element.TextGuiElement;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;

public class GuiManager {
	private final Map<GuiElementType, ElementConstructor<?>> constructors = new EnumMap<>(GuiElementType.class);
	private final IodineMod mod;
	
	public GuiManager(@NotNull IodineMod mod) {
		this.mod = mod;
		
		constructors.put(GuiElementType.TEXT, TextGuiElement::new);
		constructors.put(GuiElementType.BUTTON, ButtonGuiElement::new);
	}
	
	
	
	public void deserializeElements(@NotNull IodineGui gui,
			@NotNull Map<Integer, GuiElement> storage, @NotNull ByteBuffer buffer) {
		while (buffer.hasRemaining()) {
			GuiElementType type = GuiElementType.fromId(buffer.get());
			if (type == null) {
				mod.getLogger().error("Encountered invalid GuiElementType while deserializing");
				return;
			}
			
			GuiElement element = storage.computeIfAbsent(buffer.getInt(),
					id -> constructors.get(type).apply(gui, id));
			element.deserialize(buffer);
			element.update();
		}
	}
	
	
	
	@FunctionalInterface
	private interface ElementConstructor<R extends GuiElement> {
		R apply(IodineGui gui, int id);
	}
}
