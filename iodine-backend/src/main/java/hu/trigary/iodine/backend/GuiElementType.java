package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * A list of all element types that can be displayed.
 */
public enum GuiElementType {
	CONTAINER_GRID(0x00),
	CONTAINER_LINEAR(0x01),
	BUTTON(0x10),
	CHECKBOX(0x11),
	DROPDOWN(0x12),
	IMAGE(0x13),
	PROGRESS_BAR(0x14),
	RADIO_BUTTON(0x15),
	SLIDER(0x16),
	TEXT_FIELD(0x17),
	TEXT(0x18);
	
	private static final GuiElementType[] VALUES;
	
	static {
		VALUES = new GuiElementType[Arrays.stream(values())
				.mapToInt(type -> type.getId() & 0xFF)
				.max().orElse(-1) + 1];
		Arrays.stream(values()).forEach(type -> {
			int id = type.getId() & 0xFF;
			if (VALUES[id] != null) {
				throw new AssertionError("Multiple GuiElementTypes must not share the same ID");
			}
			VALUES[id] = type;
		});
	}
	
	private final byte id;
	
	GuiElementType(int id) {
		//not using #ordinal() for independence of declaration order (future compatibility)
		this.id = (byte) id;
	}
	
	
	
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
		return id;
	}
}
