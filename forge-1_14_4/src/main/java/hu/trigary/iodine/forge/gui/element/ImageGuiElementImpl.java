package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ImageGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The implementation of {@link ImageGuiElement}.
 */
public class ImageGuiElementImpl extends ImageGuiElement {
	private final String textureName;
	private int positionX;
	private int positionY;
	private int imageWidth;
	private int imageHeight;
	private ResourceLocation resource;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public ImageGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
		textureName = root.getId() + "#" + id;
	}
	
	
	
	@Override
	protected void updateImpl(int positionX, int positionY, int width, int height) {
		this.positionX = positionX;
		this.positionY = positionY;
		
		close();
		if (image.length == 0) {
			return;
		}
		
		try (NativeImage nativeImage = NativeImage.read(ByteBuffer.wrap(image))) {
			//yes, it is OK to close the NativeImage after creating the texture
			imageWidth = nativeImage.getWidth();
			imageHeight = nativeImage.getHeight();
			resource = Minecraft.getInstance().getRenderManager().textureManager
					.getDynamicTextureLocation(textureName, new DynamicTexture(nativeImage));
		} catch (IOException e) {
			getRoot().getMod().getLogger().error("Error loading image", e);
		}
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		if (resource == null) {
			return;
		}
		
		Minecraft.getInstance().getTextureManager().bindTexture(resource);
		GlStateManager.color4f(1, 1, 1, 1);
		//TODO implement resizing (look at NativeImage source)
		AbstractGui.blit(positionX, positionY, 0, 0, width, height, imageWidth, imageHeight);
		if (IodineGuiUtils.isInside(positionX, positionY, width, height, mouseX, mouseY)) {
			IodineGuiUtils.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public void close() {
		if (resource != null) {
			Minecraft.getInstance().getRenderManager().textureManager.deleteTexture(resource);
			resource = null;
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
