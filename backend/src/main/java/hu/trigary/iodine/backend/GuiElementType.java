package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * A list of all element types that can be displayed.
 */
public enum GuiElementType {
	CONTAINER_ROOT,
	CONTAINER_GRID,
	CONTAINER_LINEAR,
	BUTTON,
	CHECKBOX,
	CONTINUOUS_SLIDER,
	DISCRETE_SLIDER,
	DROPDOWN,
	IMAGE,
	PROGRESS_BAR,
	RADIO_BUTTON,
	RECTANGLE,
	TEXT_FIELD,
	TEXT;
	
	private static final GuiElementType[] VALUES = values();
	
	
	
	/**
	 * Gets an enum value based on the specified id.
	 * Returns null if no matching enum value was found.
	 *
	 * @param id the ID to search for
	 * @return the associated enum value or null, if none were found
	 */
	@Nullable
	@Contract(pure = true)
	public static GuiElementType fromId(byte id) {
		int index = id & 0xFF;
		return index < VALUES.length ? VALUES[index] : null;
	}
	
	/**
	 * Gets the ID of this enum value.
	 *
	 * @return the ID of this enum value
	 */
	@Contract(pure = true)
	public byte getId() {
		return (byte) ordinal();
	}
}
