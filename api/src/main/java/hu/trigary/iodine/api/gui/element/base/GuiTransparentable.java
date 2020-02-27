package hu.trigary.iodine.api.gui.element.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link GuiElement} that can have transparency applied.
 * Elements are opaque (non-transparent) by default.
 *
 * @param <T> the class implementing this interface
 */
public interface GuiTransparentable<T extends GuiTransparentable<T>> {
	/**
	 * Gets this element's current transparency as
	 * a value between 0 and 1 (both inclusive).
	 *
	 * @return the current transparency
	 */
	@Contract(pure = true)
	default float getTransparencyPercentage() {
		return getTransparencyComponent() / 255f;
	}
	
	/**
	 * Gets this element's current transparency as
	 * a value between 0x00 and 0xFF (both inclusive).
	 *
	 * @return the current transparency
	 */
	@Contract(pure = true)
	int getTransparencyComponent();
	
	/**
	 * Sets this element's transparency.
	 * The value must be at least 0 and at most 1 (both inclusive).
	 *
	 * @param transparency the new transparency
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default T setTransparencyPercentage(float transparency) {
		return setTransparencyComponent((int) (transparency * 255));
	}
	
	/**
	 * Sets this element's transparency.
	 * The value must be at least 0x00 and at most 0xFF (both inclusive).
	 *
	 * @param transparency the new transparency
	 * @return the current instance (for chaining)
	 */
	@NotNull
	T setTransparencyComponent(int transparency);
}
