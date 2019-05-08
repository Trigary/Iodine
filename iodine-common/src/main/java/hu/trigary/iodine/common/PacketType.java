package hu.trigary.iodine.common;

public enum PacketType {
	LOGIN,
	LOGIN_SUCCESS,
	LOGIN_FAILED;
	
	private static final PacketType[] VALUES = values();
	
	public static PacketType fromIndex(int index) {
		return index < VALUES.length ? VALUES[index] : null;
	}
	
	public static PacketType fromOrdinal(byte ordinal) {
		return fromIndex(ordinal & 0xFF);
	}
}
