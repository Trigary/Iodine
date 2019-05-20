package hu.trigary.iodine.api.gui;

import org.jetbrains.annotations.Contract;

/**
 * This class contains utility fields and methods regarding the sides of GUI elements.
 * The fields can be added together using the binary OR operation ({@code |}).
 */
public abstract class GuiSide {
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int TOP = 4;
	public static final int BOTTOM = 8;
	public static final int HORIZONTAL = LEFT | RIGHT;
	public static final int VERTICAL = TOP | BOTTOM;
	public static final int ALL = HORIZONTAL | VERTICAL;
	
	
	
	/**
	 * Gets whether the specified sides contain the left side.
	 *
	 * @param sides the sides to check
	 * @return whether the left side is included
	 */
	@Contract(pure = true)
	public static boolean hasLeft(int sides) {
		return (sides & LEFT) != 0;
	}
	
	/**
	 * Gets whether the specified sides contain the right side.
	 *
	 * @param sides the sides to check
	 * @return whether the right side is included
	 */
	@Contract(pure = true)
	public static boolean hasRight(int sides) {
		return (sides & RIGHT) != 0;
	}
	
	/**
	 * Gets whether the specified sides contain the top side.
	 *
	 * @param sides the sides to check
	 * @return whether the top side is included
	 */
	@Contract(pure = true)
	public static boolean hasTop(int sides) {
		return (sides & TOP) != 0;
	}
	
	/**
	 * Gets whether the specified sides contain the bottom side.
	 *
	 * @param sides the sides to check
	 * @return whether the bottom side is included
	 */
	@Contract(pure = true)
	public static boolean hasBottom(int sides) {
		return (sides & BOTTOM) != 0;
	}
}
