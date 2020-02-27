package hu.trigary.iodine.api.gui;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An immutable class representing an ARGB color.
 * Since it is immutable, the setter methods return a new instance.
 * The components' values range from 0x00 to 0xFF (both inclusive).
 * An alpha channel value of 0xFF stands for full opacity (no transparency).
 */
public final class IodineColor {
	public static final IodineColor BLACK = get(0xFF000000);
	public static final IodineColor DARK_GRAY = get(0xFF555555);
	public static final IodineColor GRAY = get(0xFFAAAAAA);
	public static final IodineColor WHITE = get(0xFFFFFFFF);
	public static final IodineColor DARK_RED = get(0xFFAA0000);
	public static final IodineColor RED = get(0xFFFF5555);
	public static final IodineColor GOLD = get(0xFFFFAA00);
	public static final IodineColor YELLOW = get(0xFFFFFF55);
	public static final IodineColor DARK_GREEN = get(0xFF00AA00);
	public static final IodineColor GREEN = get(0xFF55FF55);
	public static final IodineColor AQUA = get(0xFF55FFFF);
	public static final IodineColor DARK_AQUA = get(0xFF00AAAA);
	public static final IodineColor DARK_BLUE = get(0xFF0000AA);
	public static final IodineColor BLUE = get(0xFF5555FF);
	public static final IodineColor LIGHT_PURPLE = get(0xFFFF55FF);
	public static final IodineColor DARK_PURPLE = get(0xFFAA00AA);
	private final int argb;
	
	private IodineColor(int argb) {
		this.argb = argb;
	}
	
	/**
	 * Creates a new instance from the specified components.
	 *
	 * @param alpha the alpha component
	 * @param red the red component
	 * @param green the green component
	 * @param blue the blue component
	 * @return the new color instance containing the specified components
	 */
	@NotNull
	@Contract(pure = true, value = "_, _, _, _ -> new")
	public static IodineColor get(int alpha, int red, int green, int blue) {
		Validate.isTrue((alpha & 0xFF) == alpha, "Component values must range from 0 to 255 (both inclusive)");
		Validate.isTrue((red & 0xFF) == red, "Component values must range from 0 to 255 (both inclusive)");
		Validate.isTrue((green & 0xFF) == green, "Component values must range from 0 to 255 (both inclusive)");
		Validate.isTrue((blue & 0xFF) == blue, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor((red << 16) | (green << 8) | blue);
	}
	
	/**
	 * Creates a new instance from the specified components.
	 * The alpha channel's value will be 0xFF.
	 *
	 * @param red the red component
	 * @param green the green component
	 * @param blue the blue component
	 * @return the new color instance containing the specified components
	 */
	@NotNull
	@Contract(pure = true, value = "_, _, _ -> new")
	public static IodineColor get(int red, int green, int blue) {
		return get(0xFF, red, green, blue);
	}
	
	/**
	 * Creates a new instance from the specified integer.
	 * The 8 most significant bits hold the alpha component.
	 *
	 * @param argb the components as a single integer
	 * @return the new color instance containing the specified values
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public static IodineColor get(int argb) {
		return new IodineColor(argb);
	}
	
	
	
	/**
	 * Gets this color's components as a single integer.
	 * The 8 most significant bits hold the alpha component.
	 *
	 * @return the red component's value
	 */
	@Contract(pure = true)
	public int getArgb() {
		return argb;
	}
	
	/**
	 * Gets this color's alpha component.
	 *
	 * @return the alpha component's value
	 */
	@Contract(pure = true)
	public int getAlpha() {
		return argb >>> 24;
	}
	
	/**
	 * Gets this color's red component.
	 *
	 * @return the red component's value
	 */
	@Contract(pure = true)
	public int getRed() {
		return (argb >>> 16) & 0xFF;
	}
	
	/**
	 * Gets this color's green component.
	 *
	 * @return the green component's value
	 */
	@Contract(pure = true)
	public int getGreen() {
		return (argb >>> 8) & 0xFF;
	}
	
	/**
	 * Gets this color's blue component.
	 *
	 * @return the blue component's value
	 */
	@Contract(pure = true)
	public int getBlue() {
		return argb & 0xFF;
	}
	
	
	
	/**
	 * Creates a new instance with all components having the same value, except this one.
	 *
	 * @param alpha the component's new value
	 * @return a new instance with the correct components
	 */
	@NotNull
	@Contract(pure = true, value = "_ -> new")
	public IodineColor setAlpha(int alpha) {
		Validate.isTrue((alpha & 0xFF) == alpha, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor((argb & 0x00FFFFFF) | (alpha << 24));
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
		Validate.isTrue((red & 0xFF) == red, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor((argb & 0xFF00FFFF) | (red << 16));
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
		Validate.isTrue((green & 0xFF) == green, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor((argb & 0xFFFF00FF) | (green << 8));
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
		Validate.isTrue((blue & 0xFF) == blue, "Component values must range from 0 to 255 (both inclusive)");
		return new IodineColor((argb & 0xFFFFFF00) | blue);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return "ARGB" + Integer.toHexString(argb);
	}
	
	@Contract(pure = true)
	@Override
	public int hashCode() {
		return 131 + 127 * argb;
	}
	
	@Contract(pure = true)
	@Override
	public boolean equals(Object object) {
		return object instanceof IodineColor && argb == ((IodineColor) object).argb;
	}
}
