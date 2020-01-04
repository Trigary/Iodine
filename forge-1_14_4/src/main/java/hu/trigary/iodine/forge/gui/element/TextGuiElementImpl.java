package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.TextGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;

public class TextGuiElementImpl extends TextGuiElement {
	private static final int COLOR = 0xFFFFFF; //16777215
	private final FontRenderer fontRenderer;
	private int offset;
	
	public TextGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
		//noinspection resource
		fontRenderer = Minecraft.getInstance().fontRenderer;
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		if (alignment != 4) {
			offset = width - fontRenderer.getStringWidth(text);
			if (alignment == 5) {
				offset /= 2;
			}
		}
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		fontRenderer.drawStringWithShadow(text, positionX + offset, positionY, COLOR);
	}
}
