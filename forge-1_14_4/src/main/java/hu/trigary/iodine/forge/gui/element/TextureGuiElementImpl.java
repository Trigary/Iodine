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
	private int positionX;
	private int positionY;
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
		this.positionX = positionX;
		this.positionY = positionY;
		resource = new ResourceLocation(texture);
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		Minecraft.getInstance().getTextureManager().bindTexture(resource);
		GlStateManager.color4f(1, 1, 1, 1);
		//TODO implement resizing (look at NativeImage source)
		AbstractGui.blit(positionX, positionY, width, height, offsetX, offsetY,
				textureWidth, textureHeight, fileWidth, fileHeight);
		if (IodineGuiUtils.isInside(positionX, positionY, width, height, mouseX, mouseY)) {
			IodineGuiUtils.renderTooltip(mouseX, mouseY, tooltip);
		}
	}



	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		if (IodineGuiUtils.isInside(positionX, positionY, width, height, mouseX, mouseY)) {
			onChanged();
			return true;
		}
		return false;
	}
}
