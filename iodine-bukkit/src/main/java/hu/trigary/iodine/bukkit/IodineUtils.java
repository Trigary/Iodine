package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.gui.element.base.GuiHeightSettable;
import hu.trigary.iodine.api.gui.element.base.GuiWidthSettable;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of static utility methods used internally.
 */
public final class IodineUtils {
	private IodineUtils() {}
	
	
	
	/**
	 * Validates that a given value is between its bounds.
	 *
	 * @param minInclusive the lower bound, inclusive
	 * @param maxInclusive the upper bound, inclusive
	 * @param value the value to validate
	 * @param parameter the name of the value
	 */
	@Contract(pure = true)
	public static void validateRange(int minInclusive, int maxInclusive, int value, @NotNull String parameter) {
		Validate.isTrue(value >= minInclusive && value <= maxInclusive,
				parameter + " must be at least " + minInclusive + " and at most " + maxInclusive + " (both inclusive)");
	}
	
	/**
	 * Validates that a given value is a valid width value.
	 *
	 * @param width the width to validate
	 */
	@Contract(pure = true)
	public static void validateWidth(int width) {
		validateRange(1, GuiWidthSettable.WIDTH_UPPER_BOUND, width, "width");
	}
	
	/**
	 * Validates that a given value is a valid height value.
	 *
	 * @param height the height to validate
	 */
	@Contract(pure = true)
	public static void validateHeight(int height) {
		validateRange(1, GuiHeightSettable.HEIGHT_UPPER_BOUND, height, "width");
	}
}
