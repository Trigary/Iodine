package hu.trigary.iodine.server.network;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A buffer which is able to resize itself as necessary.
 */
public final class ResizingByteBuffer {
	private final ByteArrayOutputStream stream;
	
	/**
	 * Creates a new buffer with the specified initial size.
	 *
	 * @param initialCapacity the starting byte capacity of the buffer
	 */
	public ResizingByteBuffer(int initialCapacity) {
		//There doesn't seem to be a server -> client packet length limit
		stream = new ByteArrayOutputStream(initialCapacity);
	}
	
	
	
	/**
	 * Copies all written bytes into a array and resets the write index,
	 * causing all subsequent writes to overwrite the previous data.
	 *
	 * @return the previously written bytes
	 */
	@NotNull
	public byte[] toArrayAndReset() {
		byte[] array = stream.toByteArray();
		stream.reset();
		return array;
	}
	
	
	
	/**
	 * Puts the specified byte into this buffer.
	 *
	 * @param value the value to write
	 */
	public void putByte(byte value) {
		stream.write(value);
	}
	
	/**
	 * Puts the specified bytes into this buffer.
	 * The count of bytes is not written.
	 *
	 * @param value the value to write
	 */
	public void putBytes(@NotNull byte[] value) {
		for (byte b : value) {
			stream.write(b);
		}
	}
	
	/**
	 * Puts the specified short into this buffer as 2 bytes.
	 *
	 * @param value the value to write
	 */
	public void putShort(short value) {
		stream.write(value >>> 8);
		stream.write(value & 0xFF);
	}
	
	/**
	 * Puts the specified int into this buffer as 4 bytes.
	 *
	 * @param value the value to write
	 */
	public void putInt(int value) {
		stream.write(value >>> 24);
		stream.write((value >>> 16) & 0xFF);
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}
	
	/**
	 * Puts the specified float into this buffer as 4 bytes.
	 *
	 * @param value the value to write
	 */
	public void putFloat(float value) {
		putInt(Float.floatToRawIntBits(value));
	}
	
	/**
	 * Puts the specified boolean into this buffer as a bytes.
	 *
	 * @param value the value to write
	 */
	public void putBool(boolean value) {
		stream.write(value ? 1 : 0);
	}
	
	/**
	 * Puts the specified String into this buffer by converting it to an UTF-8 encoded byte array.
	 * The length of this array (as a short) and the array itself is then put into the buffer.
	 *
	 * @param value the value to write
	 */
	public void putString(@NotNull String value) {
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		putShort((short) bytes.length);
		putBytes(bytes);
	}
}
