package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * The manager whose responsibility is created new {@link GuiElement} instances.
 */
public abstract class ElementManager {
	private final Map<GuiElementType, ElementConstructor<?>> constructors = new EnumMap<>(GuiElementType.class);
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link hu.trigary.iodine.client.IodineMod}.
	 */
	protected ElementManager() {
		for (GuiElementType type : GuiElementType.values()) {
			constructors.put(type, getElementConstructor(type));
		}
	}
	
	
	
	/**
	 * Gets the instance of the element which has the ID specified in the buffer.
	 * This element can be a new instance or an already existing one.
	 * Whatever the case, the element will be stored in the ID-instance map.
	 *
	 * @param root the instance that contains the element
	 * @param storage the ID to instance map containing the existing elements
	 * @param buffer the buffer containing the serialized element
	 * @return the instance with the ID specified in the buffer
	 */
	@NotNull
	public final GuiElement getElement(@NotNull IodineRoot root,
			@NotNull Map<Integer, GuiElement> storage, @NotNull ByteBuffer buffer) {
		GuiElementType type = GuiElementType.fromId(buffer.get());
		Validate.notNull(type, "Encountered invalid GuiElementType while deserializing");
		int id = buffer.getInt();
		return storage.computeIfAbsent(id, ignored -> constructors.get(type).apply(root, id));
	}
	
	/**
	 * Gets the constructor of the specified type.
	 * The results are internally cached.
	 *
	 * @param type the type to get the constructor of
	 * @return the constructor of the specified type.
	 */
	@NotNull
	protected abstract ElementConstructor<?> getElementConstructor(@NotNull GuiElementType type);
	
	
	
	/**
	 * A functional interface that constructs a new instance of an element.
	 *
	 * @param <T> the type of the element
	 */
	@FunctionalInterface
	protected interface ElementConstructor<T extends GuiElement> {
		/**
		 * Creates a new instance of an element type.
		 *
		 * @param root the instance that contains the element
		 * @param id the element's ID
		 * @return the new instance
		 */
		T apply(@NotNull IodineRoot root, int id);
	}
}
