package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.TextureGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link TextureGuiElement}.
 */
public class TextureGuiElementImpl extends TextureGuiElement {
	private ResourceLocation resource;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public TextureGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int positionX, int positionY, int width, int height) {
		resource = new ResourceLocation(texture);
		System.out.println(textureWidth + " " + textureHeight + " " + fileWidth + " " + fileHeight);
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		Minecraft.getInstance().getTextureManager().bindTexture(resource);
		GlStateManager.color4f(1, 1, 1, 1);
		
		if (interpolating) {
			AbstractGui.blit(positionX, positionY, width, height, offsetX, offsetY,
					textureWidth, textureHeight, fileWidth, fileHeight);
		} else {
			int columns = width / textureWidth;
			int rows = height / textureHeight;
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					AbstractGui.blit(positionX + x * textureWidth, positionY + y * textureHeight,
							textureWidth, textureHeight, offsetX, offsetY,
							textureWidth, textureHeight, fileWidth, fileHeight);
				}
			}
			
			int widthLeft = width % textureWidth;
			int heightLeft = height % textureHeight;
			AbstractGui.blit(positionX + columns * textureWidth, positionY + rows * textureHeight,
					widthLeft, heightLeft, offsetX, offsetY, widthLeft, heightLeft, fileWidth, fileHeight);
			for (int x = 0; x < columns; x++) {
				AbstractGui.blit(positionX + x * textureWidth, positionY + rows * textureHeight,
						textureWidth, heightLeft, offsetX, offsetY, textureWidth, heightLeft, fileWidth, fileHeight);
			}
			for (int y = 0; y < rows; y++) {
				AbstractGui.blit(positionX + columns * textureWidth, positionY + y * textureHeight,
						widthLeft, textureHeight, offsetX, offsetY, widthLeft, textureHeight, fileWidth, fileHeight);
			}
		}
	}
	
	@Override
	protected void drawTooltipImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY) {
		if (IodineGuiUtils.isInside(positionX, positionY, width, height, mouseX, mouseY)) {
			IodineGuiUtils.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		if (IodineGuiUtils.isInside(getPositionX(), getPositionY(), width, height, mouseX, mouseY)) {
			onChanged();
			return true;
		}
		return false;
	}
}
