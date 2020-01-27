package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.TextGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link TextGuiElement}.
 */
public class TextGuiElementImpl extends TextGuiElement {
	private static final int COLOR = 0xFFFFFF; //16777215
	private final FontRenderer fontRenderer;
	private int offset;

	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public TextGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
		fontRenderer = Minecraft.getInstance().fontRenderer;
	}
	
	
	
	@Override
	protected void updateImpl(int positionX, int positionY, int width, int height) {
		if (alignment != 4) {
			offset = width - fontRenderer.getStringWidth(text);
			if (alignment == 5) {
				offset /= 2;
			}
		}
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		fontRenderer.drawStringWithShadow(text, positionX + offset, positionY, COLOR);
	}
}
