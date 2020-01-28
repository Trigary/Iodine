package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A buffer that can be read from.
 * Data (to later read) can be linked to this instance as {@code byte[]}.
 */
public final class InputBuffer {
	private ByteBuffer buffer;
	
	
	
	/**
	 * Links data to this buffer so that it can be read.
	 * The array specified as the parameter is not copied,
	 * therefore it must not be modified after calling this method.
	 *
	 * @param data the data to store
	 * @param skip an offset, the count of bytes to skip at the start
	 * @return the current instance, for chaining
	 */
	@NotNull
	public InputBuffer update(@NotNull byte[] data, int skip) {
		buffer = ByteBuffer.wrap(data, skip, data.length - skip);
		return this;
	}
	
	/**
	 * Returns whether there are any bytes that have been set but not yet read.
	 *
	 * @return true if there is at least one byte that was set but not read, false otherwise
	 */
	@Contract(pure = true)
	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}
	
	
	
	/**
	 * Reads a byte from this buffer.
	 *
	 * @return the deserialized value
	 */
	public byte readByte() {
		return buffer.get();
	}
	
	/**
	 * Reads a short from this buffer as 2 bytes.
	 *
	 * @return the deserialized value
	 */
	public short readShort() {
		return buffer.getShort();
	}
	
	/**
	 * Reads a int from this buffer as 4 bytes.
	 *
	 * @return the deserialized value
	 */
	public int readInt() {
		return buffer.getInt();
	}
	
	/**
	 * Reads a float from this buffer as 4 bytes.
	 *
	 * @return the deserialized value
	 */
	public float readFloat() {
		return buffer.getFloat();
	}
	
	/**
	 * Reads a boolean from this buffer as 1 byte.
	 *
	 * @return the deserialized value
	 */
	public boolean readBool() {
		int value = buffer.get();
		if (value == 0) {
			return false;
		} else if (value == 1) {
			return true;
		} else {
			throw new AssertionError("Unexpected byte value when deserializing boolean: " + value);
		}
	}
	
	/**
	 * Deserializes an UTF-8 string from the buffer.
	 * First the string's encoded length is read from the buffer,
	 * then the encoded string itself, which is then decoded as UTF-8.
	 *
	 * @return the deserialized value
	 */
	@NotNull
	public String readString() {
		//noinspection ConstantConditions
		return readString(Integer.MAX_VALUE);
	}
	
	/**
	 * Deserializes an UTF-8 string from the buffer.
	 * First the string's encoded length is read from the buffer,
	 * then the encoded string itself, which is then decoded as UTF-8.
	 * Null is returned if the length is greater than the limit value.
	 *
	 * @param maxByteCount the maximum count of bytes to accept
	 * @return the deserialized value or null if the read length exceeds the limit
	 */
	@Nullable
	public String readString(int maxByteCount) {
		int length = readShort();
		if (length > maxByteCount) {
			return null;
		}
		
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
