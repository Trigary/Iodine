package hu.trigary.iodine.client;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class IntPair {
	private final int x;
	private final int y;
	
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	@Contract(pure = true)
	public int getX() {
		return x;
	}
	
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
