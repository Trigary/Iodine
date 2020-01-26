package hu.trigary.iodine.api.gui.element;

import hu.trigary.iodine.api.gui.element.base.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A GUI element that displays a texture and can be clicked.
 * The texture can be from vanilla Minecraft texture or from a resource pack.
 */
public interface TextureGuiElement extends GuiElement<TextureGuiElement>,
		GuiWidthSettable<TextureGuiElement>, GuiHeightSettable<TextureGuiElement>,
		GuiTooltipable<TextureGuiElement>, GuiClickable<TextureGuiElement>,
		GuiResizable<TextureGuiElement> {
	/**
	 * Gets the texture's identifier.
	 *
	 * @return the texture identifier
	 */
	@NotNull
	@Contract(pure = true)
	String getTexture();
	
	/**
	 * Gets the texture file's width, height, the desired texture's X and Y offset inside the file
	 * and the desired texture's width and height, in this order.
	 * All of these values are 0 by default.
	 *
	 * @return an array containing exactly the specified values
	 */
	@NotNull
	@Contract(pure = true)
	int[] getTextureData();
	
	
	
	/**
	 * Sets the texture to use.
	 * This texture and the file containing it must have the same width and height as this element.
	 *
	 * @param namespacedKey the texture's namespace and location, separated by ':'
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default TextureGuiElement setTexture(@NotNull String namespacedKey) {
		return setTexture(namespacedKey, 0, 0);
	}
	
	/**
	 * Sets the texture to use.
	 * This texture and the file containing it must share the same width and height.
	 *
	 * @param namespacedKey the texture's namespace and location, separated by ':'
	 * @param textureWidth the texture's and its file's width
	 * @param textureHeight the texture's and its file's height
	 * @return the current instance (for chaining)
	 */
	@NotNull
	default TextureGuiElement setTexture(@NotNull String namespacedKey, int textureWidth, int textureHeight) {
		return setTexture(namespacedKey, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
	}
	
	/**
	 * Sets the texture to use.
	 *
	 * @param namespacedKey the texture's namespace and location, separated by ':'
	 * @param textureFileWidth the texture file's width
	 * @param textureFileHeight the texture file's height
	 * @param textureOffsetX the texture's X offset inside its containing file
	 * @param textureOffsetY the texture's Y offset inside its containing file
	 * @param textureWidth the texture's width
	 * @param textureHeight the texture's height
	 * @return the current instance (for chaining)
	 */
	@NotNull
	TextureGuiElement setTexture(@NotNull String namespacedKey, int textureFileWidth, int textureFileHeight,
			int textureOffsetX, int textureOffsetY, int textureWidth, int textureHeight);
}
