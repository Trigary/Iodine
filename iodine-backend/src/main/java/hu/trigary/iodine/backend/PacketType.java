package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * The prefix (CLIENT/SERVER) stands for the party which sends the packet type.
 * A single packet type can't be both send and received by the same party.
 */
public enum PacketType {
	CLIENT_LOGIN,
	SERVER_LOGIN_SUCCESS,
	SERVER_LOGIN_FAILED,
	SERVER_GUI_OPEN,
	SERVER_GUI_CLOSE,
	SERVER_GUI_CHANGE,
	CLIENT_GUI_CLOSE,
	CLIENT_GUI_CHANGE;
	
	private static final PacketType[] VALUES = values();
	
	@Nullable
	@Contract(pure = true)
	public static PacketType fromOrdinal(byte ordinal) {
		int index = ordinal & 0xFF;
		return index < VALUES.length ? VALUES[index] : null;
	}
}
