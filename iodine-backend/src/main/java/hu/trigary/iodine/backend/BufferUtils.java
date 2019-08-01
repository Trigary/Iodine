package hu.trigary.iodine.backend;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A collection of utilities which allow the serialization and deserialization
 * of types not directly supported by {@link ByteBuffer}.
 */
public final class BufferUtils {
	private BufferUtils() {}
	
	
	
	/**
	 * Serializes the specified boolean as a byte.
	 * True values are serialized as {@code (byte)1},
	 * while false values are serialized as {@code (byte)0}.
	 *
	 * @param buffer the buffer to serialize into
	 * @param value the value to serialize
	 */
	public static void serializeBoolean(@NotNull ByteBuffer buffer, boolean value) {
		buffer.put(value ? (byte) 1 : 0);
	}
	
	/**
	 * Serializes the specified text by converting it to an UTF-8 encoded byte array.
	 * The length of this array (as a short) and the array itself is then put into the buffer.
	 *
	 * @param buffer the buffer to serialize into
	 * @param value the value to serialize
	 */
	public static void serializeString(@NotNull ByteBuffer buffer, @NotNull String value) {
		byte[] textBytes = value.getBytes(StandardCharsets.UTF_8);
		buffer.putShort((short) textBytes.length);
		buffer.put(textBytes);
	}
	
	
	
	/**
	 * Deserializes the next byte as a boolean.
	 * {@code (byte)0} and {@code (byte)1} are deserialized as false and true respectively.
	 * All other byte values cause an exception to be thrown.
	 *
	 * @param buffer the buffer to deserialize from
	 * @return the deserialized value
	 */
	public static boolean deserializeBoolean(@NotNull ByteBuffer buffer) {
		int value = buffer.get();
		if (value == 0) {
			return false;
		} else if (value == 1) {
			return true;
		} else {
			throw new RuntimeException("Unexpected byte value when deserializing boolean: " + value);
		}
	}
	
	/**
	 * Deserializes an UTF-8 string from the buffer.
	 * The first 2 bytes indicate the length of the byte array
	 * that is then interpreted as an UTF-8 string.
	 *
	 * @param buffer the buffer to deserialize from
	 * @return the deserialized value
	 */
	@NotNull
	public static String deserializeString(@NotNull ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.getShort()];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
