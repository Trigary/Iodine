package hu.trigary.iodine.client.util;

import org.jetbrains.annotations.Contract;

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
}
