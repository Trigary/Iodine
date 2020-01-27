package hu.trigary.iodine.forge.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utilities which help the drawing of GUIs.
 */
public final class IodineGuiUtils {
	private IodineGuiUtils() {}
	
	
	
	/**
	 * Renders the specified tooltip if it's not an empty {@link String}.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param tooltip the tooltip to render
	 */
	public static void renderTooltip(int mouseX, int mouseY, @NotNull String tooltip) {
		if (tooltip.isEmpty()) {
			return;
		}

		Screen screen = Minecraft.getInstance().currentScreen;
		if (screen instanceof IodineGuiScreen) {
			screen.renderTooltip(tooltip, mouseX, mouseY);
			GlStateManager.disableLighting();
		} else {
			throw new AssertionError("Attempted to render tooltip when no screens or a non-Iodine screen was open.");
		}
	}
	
	/**
	 * Gets whether the mouse is inside a rectangular area.
	 *
	 * @param positionX the rectangle's X position
	 * @param positionY the rectangle's X position
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @return {@code true} if the mouse is inside the specified rectangle
	 */
	@Contract(pure = true)
	public static boolean isInside(int positionX, int positionY, int width, int height, double mouseX, double mouseY) {
		return mouseX >= positionX && mouseY >= positionY && mouseX < positionX + width && mouseY < positionY + height;
	}
}
