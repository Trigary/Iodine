package hu.trigary.iodine.client;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An immutable class that represents a pair a of {@code int}s.
 */
public final class IntPair {
	private final int x;
	private final int y;
	
	/**
	 * Creates a new instance of this immutable class.
	 *
	 * @param x the first value
	 * @param y the second value
	 */
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	/**
	 * Gets the first value.
	 *
	 * @return the x value
	 */
	@Contract(pure = true)
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the second value.
	 *
	 * @return the y value
	 */
	@Contract(pure = true)
	public int getY() {
		return y;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return x + ";" + y;
	}
	
	@Contract(pure = true)
	@Override
	public int hashCode() {
		return 53 * (x ^ y);
	}
	
	@Contract(pure = true)
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IntPair)) {
			return false;
		}
		IntPair other = (IntPair) object;
		return other.x == x && other.y == y;
	}
}
