package hu.trigary.iodine.api.gui;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An immutable class representing an RGB color.
 * Since it is immutable, the setter methods return a new instance.
 * The components' values range from 0x0 to 0xFF.
 */
public final class IodineColor {
	public static final IodineColor BLACK = get(0x000000);
	public static final IodineColor DARK_GRAY = get(0x555555);
	public static final IodineColor GRAY = get(0xAAAAAA);
	public static final IodineColor WHITE = get(0xFFFFFF);
	public static final IodineColor DARK_RED = get(0xAA0000);
	public static final IodineColor RED = get(0xFF5555);
	public static final IodineColor GOLD = get(0xFFAA00);
	public static final IodineColor YELLOW = get(0xFFFF55);
	public static final IodineColor DARK_GREEN = get(0x00AA00);
	public static final IodineColor GREEN = get(0x55FF55);
	public static final IodineColor AQUA = get(0x55FFFF);
	public static final IodineColor DARK_AQUA = get(0x00AAAA);
	public static final IodineColor DARK_BLUE = get(0x0000AA);
	public static final IodineColor BLUE = get(0x5555FF);
	public static final IodineColor LIGHT_PURPLE = get(0xFF55FF);
	public static final IodineColor DARK_PURPLE = get(0xAA00AA);
	private final int red;
	private final int green;
	private final int blue;
	
	private IodineColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	/**
	 * Creates a new instance from the specified components.
	 *
	 * @param red the red component
	 * @param green the green component
	 * @param blue the blue component
	 * @return the new color instance containing the specified components
	 */
	@NotNull
	@Contract(pure = true, value = "_, _, _ -> new")
	public static IodineColor get(int red, int green, int blue) {
		Validate.isTrue((red & 0xFF) == red, "Component values must range from 0 to 255 (both inclusive)");
		Validate.isTrue((green & 0xFF) == green, "Component values must range from 0 to 255 (both inclusive)");
		Validate.isTrue((blue & 0xFF) == blue, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor(red, green, blue);
	}
	
	/**
	 * Creates a new instance from the specified integer.
	 * The 8 most significant bits must not be set.
	 *
	 * @param rgb the components as a single integer
	 * @return the new color instance containing the specified values
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public static IodineColor get(int rgb) {
		Validate.isTrue(rgb >>> 24 == 0, "The 8 most significant bits must not be set");
		return new IodineColor(rgb >>> 16, (rgb >>> 8) & 0xFF, rgb & 0xFF);
	}
	
	
	
	/**
	 * Gets this color's red component.
	 *
	 * @return the red component's value
	 */
	@Contract(pure = true)
	public int getRed() {
		return red;
	}
	
	/**
	 * Gets this color's green component.
	 *
	 * @return the green component's value
	 */
	@Contract(pure = true)
	public int getGreen() {
		return green;
	}
	
	/**
	 * Gets this color's blue component.
	 *
	 * @return the blue component's value
	 */
	@Contract(pure = true)
	public int getBlue() {
		return blue;
	}
	
	
	
	/**
	 * Creates a new instance with all components having the same value, except this one.
	 *
	 * @param red the component's new value
	 * @return a new instance with the correct components
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public IodineColor setRed(int red) {
		return get(red, green, blue);
	}
	
	/**
	 * Creates a new instance with all components having the same value, except this one.
	 *
	 * @param green the component's new value
	 * @return a new instance with the correct components
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public IodineColor setGreen(int green) {
		return get(red, green, blue);
	}
	
	/**
	 * Creates a new instance with all components having the same value, except this one.
	 *
	 * @param blue the component's new value
	 * @return a new instance with the correct components
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public IodineColor setBlue(int blue) {
		return get(red, green, blue);
	}
}
