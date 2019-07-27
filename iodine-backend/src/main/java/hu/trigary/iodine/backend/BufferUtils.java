package hu.trigary.iodine.backend;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class BufferUtils {
	private BufferUtils() {}
	
	
	
	/**
	 * Serializes the specified boolean as a byte.
	 * True values are serialized as {@code (byte)1},
	 * while false values are serialized as {@code (byte)0}.
	 *
	 * @param buffer the buffer to serialize into
	 * @param bool the boolean to serialize
	 */
	public static void serializeBoolean(@NotNull ByteBuffer buffer, boolean bool) {
		buffer.put(bool ? (byte) 1 : 0);
	}
	
	/**
	 * Serializes the specified text by converting it to an UTF-8 encoded by byte array.
	 * The length of this array and the array itself is then put into the buffer.
	 *
	 * @param buffer the buffer to serialize into
	 * @param string the text to serialize
	 */
	public static void serializeString(@NotNull ByteBuffer buffer, @NotNull String string) {
		byte[] textBytes = string.getBytes(StandardCharsets.UTF_8);
		buffer.putInt(textBytes.length);
		buffer.put(textBytes);
	}
	
	
	
	//TODO javadocs
	public static boolean deserializeBoolean(@NotNull ByteBuffer buffer) {
		return buffer.get() == (byte) 1;
	}
	
	@NotNull
	public static String deserializeString(@NotNull ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.getInt()];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
