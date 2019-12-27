package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.Validator;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class GuiElementManager {
	private final Map<GuiElementType, ElementConstructor<?>> constructors = new EnumMap<>(GuiElementType.class);
	private final IodineMod mod;
	
	protected GuiElementManager(@NotNull IodineMod mod) {
		this.mod = mod;
		for (GuiElementType type : GuiElementType.values()) {
			constructors.put(type, getElementConstructor(type));
		}
	}
	
	
	
	@NotNull
	public final GuiElement getElement(@NotNull GuiBase gui,
			@NotNull Map<Integer, GuiElement> storage, @NotNull ByteBuffer buffer) {
		GuiElementType type = GuiElementType.fromId(buffer.get());
		Validator.notNull(type, "Encountered invalid GuiElementType while deserializing");
		int id = buffer.getInt();
		return storage.computeIfAbsent(id, ignored -> constructors.get(type).apply(gui, id));
	}
	
	@NotNull
	protected abstract ElementConstructor<?> getElementConstructor(@NotNull GuiElementType type);
	
	
	
	@FunctionalInterface
	protected interface ElementConstructor<R extends GuiElement> {
		R apply(@NotNull GuiBase gui, int id);
	}
}
