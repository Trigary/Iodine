package hu.trigary.iodine.backend;

import org.jetbrains.annotations.NotNull;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A buffer that can be written to.
 * The written data can be retrieved as a {@code byte[]}.
 * A tiny bit less than 32 KBs (2^15 bytes) can be written in total,
 * this limit is present due to the plugin message length limit.
 */
public final class OutputBuffer {
	private final ByteBuffer buffer = ByteBuffer.wrap(new byte[32766]);
	
	
	
	/**
	 * Copies all written bytes into a array and resets the write index,
	 * causing all subsequent writes to overwrite the previous data.
	 *
	 * @return the previously written bytes
	 */
	@NotNull
	public byte[] toArrayAndReset() {
		//This is a minefield, gotta love signature changes between Java versions
		((Buffer) buffer).flip();
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		((Buffer) buffer).clear();
		return result;
	}
	
	
	
	/**
	 * Puts the specified byte into this buffer.
	 *
	 * @param value the value to write
	 */
	public void putByte(byte value) {
		buffer.put(value);
	}
	
	/**
	 * Puts the specified short into this buffer as 2 bytes.
	 *
	 * @param value the value to write
	 */
	public void putShort(short value) {
		buffer.putShort(value);
	}
	
	/**
	 * Puts the specified int into this buffer as 4 bytes.
	 *
	 * @param value the value to write
	 */
	public void putInt(int value) {
		buffer.putInt(value);
	}
	
	/**
	 * Puts the specified float into this buffer as 4 bytes.
	 *
	 * @param value the value to write
	 */
	public void putFloat(float value) {
		buffer.putFloat(value);
	}
	
	/**
	 * Puts the specified boolean into this buffer as 1 byte.
	 *
	 * @param value the value to write
	 */
	public void putBool(boolean value) {
		buffer.put((byte) (value ? 1 : 0));
	}
	
	/**
	 * Puts the specified String into this buffer by converting it to an UTF-8 encoded byte array.
	 * The length of this array (as a short) and then the array itself is put into the buffer.
	 *
	 * @param value the value to write
	 */
	public void putString(@NotNull String value) {
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		putShort((short) bytes.length);
		buffer.put(bytes);
	}
}
