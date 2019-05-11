package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * A list of all packet types which can be sent through the messaging channel.
 * <br><br>
 * The prefix (CLIENT/SERVER) stands for the party which sends the packet type.
 * A single packet type can't be both sent and received by the same party.
 */
public enum PacketType {
	/**
	 * Sent by the client as soon as possible to indicate that it has the mod installed.
	 * The payload is the client's mod version string.
	 */
	CLIENT_LOGIN(0x00),
	
	/**
	 * Sent as a reply to {@link #CLIENT_LOGIN}, indicating that the client's mod
	 * and the server's plugin are compatible protocol version-wise.
	 * There is no payload.
	 */
	SERVER_LOGIN_SUCCESS(0x01),
	
	/**
	 * Sent as a reply to {@link #CLIENT_LOGIN}, indicating that the client's mod
	 * and the server's plugin are incompatible protocol version-wise.
	 * Contains whether the client or the server is using the older version.
	 */
	SERVER_LOGIN_FAILED(0x02),
	
	/**
	 * Instructs the client to open the GUI specified in the payload.
	 */
	SERVER_GUI_OPEN(0x10),
	
	/**
	 * Instructs the client to close the currently open GUI,
	 * if its ID matches the one in the payload.
	 */
	SERVER_GUI_CLOSE(0x11),
	
	/**
	 * Instructs the client to update its GUI based on the changes specified in the payload.
	 */
	SERVER_GUI_CHANGE(0x12),
	
	/**
	 * Informs the server that the player has closed the GUI.
	 * The payload contains the GUI's ID.
	 */
	CLIENT_GUI_CLOSE(0x21),
	
	/**
	 * Informs the server that the player has changed a value in the GUI.
	 * The payload contains the changes.
	 */
	CLIENT_GUI_CHANGE(0x22);
	
	/**
	 * The name of the messaging channel that this plugin and mod use.
	 */
	public static final String NETWORK_CHANNEL = "hu.trigary:iodine";
	private static final PacketType[] VALUES;
	
	static {
		VALUES = new PacketType[Arrays.stream(values())
				.mapToInt(PacketType::getUnsignedId)
				.max().orElse(-1) + 1];
		Arrays.stream(values()).forEach(type -> {
			if (VALUES[type.getUnsignedId()] != null) {
				throw new AssertionError("Multiple PacketTypes must not share the same ID");
			}
			VALUES[type.getUnsignedId()] = type;
		});
	}
	
	private final byte id;
	
	PacketType(int id) {
		//not using #ordinal() for independence of declaration order (future compatibility)
		this.id = (byte) id;
	}
	
	
	
	/**
	 * Gets the packet ID, which has the highest unsigned value.
	 *
	 * @return the value of the highest unsigned packet ID
	 */
	@Contract(pure = true)
	public static int getHighestId() {
		return VALUES.length - 1;
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
	public static PacketType fromId(byte id) {
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
	
	/**
	 * Gets the ID of this enum value,
	 * when interpreted as an unsigned integer.
	 *
	 * @return the ID of this enum value
	 * @see #fromId(byte)
	 */
	@Contract(pure = true)
	public int getUnsignedId() {
		return id & 0xFF;
	}
}
