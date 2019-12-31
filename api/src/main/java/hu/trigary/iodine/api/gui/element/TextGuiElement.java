package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.api.gui.element.base.GuiTextable;
import hu.trigary.iodine.api.gui.element.base.GuiWidthSettable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays text.
 */
public interface TextGuiElement extends GuiElement<TextGuiElement>,
		GuiWidthSettable<TextGuiElement>, GuiTextable<TextGuiElement> {
	/**
	 * Gets where the text is positioned inside the element.
	 * The default alignment is {@link Alignment#CENTER}.
	 *
	 * @return the current alignment
	 */
	@NotNull
	@Contract(pure = true)
	Alignment getAlignment();
	
	/**
	 * Sets where the text should be positioned inside this element.
	 *
	 * @param alignment the new alignment
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextGuiElement setAlignment(@NotNull Alignment alignment);
	
	
	
	/**
	 * Represents directions to which texts can be aligned.
	 */
	enum Alignment {
		LEFT(4),
		CENTER(5),
		RIGHT(6);
		
		private final int number;
		
		Alignment(int number) {
			this.number = number;
		}
		
		/**
		 * Gets the numpad number whose position represents this alignment.
		 *
		 * @return the numpad number representing the alignment
		 */
		@Contract(pure = true)
		public int getNumber() {
			return number;
		}
	}
}
