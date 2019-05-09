package hu.trigary.iodine.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum PacketType {
	LOGIN,
	LOGIN_SUCCESS,
	LOGIN_FAILED;
	
	private static final PacketType[] VALUES = values();
	
	@Nullable
	@Contract(pure = true)
	public static PacketType fromIndex(int index) {
		return index < VALUES.length ? VALUES[index] : null;
	}
	
	@Nullable
	@Contract(pure = true)
	public static PacketType fromOrdinal(byte ordinal) {
		return fromIndex(ordinal & 0xFF);
	}
}
