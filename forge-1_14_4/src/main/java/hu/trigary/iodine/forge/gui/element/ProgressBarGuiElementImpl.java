package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ProgressBarGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ProgressBarGuiElement}.
 */
public class ProgressBarGuiElementImpl extends ProgressBarGuiElement {
	private static final ResourceLocation TEXTURE = new ResourceLocation("iodine", "progress-bar.png");
	private static final int CAP_SIZE = 10;
	private static final int CENTER_SIZE = 128 - 2 * CAP_SIZE;
	private ProgressBar widget;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public ProgressBarGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new ProgressBar(positionX, positionY, width, height, verticalOrientation, progress);
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
		if (widget.isHovered()) {
			IodineGuiScreen.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	private static class ProgressBar extends Widget {
		private final boolean vertical;
		private final int amount;
		private final int progressAmount;
		private final int thickness;
		
		ProgressBar(int x, int y, int width, int height, boolean verticalOrientation, float progress) {
			super(x, y, width, height, "");
			vertical = verticalOrientation;
			amount = vertical ? height : width;
			progressAmount = (int) (amount * progress);
			thickness = verticalOrientation ? width : height;
		}
		
		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			//noinspection resource
			Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
			GlStateManager.color4f(1, 1, 1, alpha);
			
			int remainingAmount = amount - 2 * CAP_SIZE;
			int offset = CAP_SIZE;
			int remainingProgressAmount = progressAmount - CAP_SIZE;
			int progressOffset = CAP_SIZE;
			
			draw(vertical, x, y, 0, CAP_SIZE, amount, thickness, 0, 0, 128, 32);
			if (progressAmount > 0) {
				draw(vertical, x, y, 0, Math.min(CAP_SIZE, progressAmount), amount, thickness, 0, 16, 128, 32);
			}
			
			draw(vertical, x, y, amount - CAP_SIZE, CAP_SIZE, amount, thickness, CAP_SIZE + CENTER_SIZE, 0, 128, 32);
			if (progressAmount > amount - CAP_SIZE) {
				int temp = progressAmount - amount + CAP_SIZE;
				draw(vertical, x, y, amount - CAP_SIZE, temp, amount, thickness, CAP_SIZE + CENTER_SIZE, 16, 128, 32);
				remainingProgressAmount -= temp;
			}
			
			while (remainingAmount > CENTER_SIZE) {
				draw(vertical, x, y, offset, CENTER_SIZE, amount, thickness, CAP_SIZE, 0, 128, 32);
				remainingAmount -= CENTER_SIZE;
				offset += CENTER_SIZE;
			}
			while (remainingProgressAmount > CENTER_SIZE) {
				draw(vertical, x, y, progressOffset, CENTER_SIZE, amount, thickness, CAP_SIZE, 16, 128, 32);
				remainingProgressAmount -= CENTER_SIZE;
				progressOffset += CENTER_SIZE;
			}
			
			if (remainingAmount > 0) {
				draw(vertical, x, y, offset, remainingAmount, amount, thickness, CAP_SIZE, 0, 128, 32);
			}
			if (remainingProgressAmount > 0) {
				draw(vertical, x, y, progressOffset, remainingProgressAmount, amount, thickness, CAP_SIZE, 16, 128, 32);
			}
		}
		
		private static void draw(boolean vertical, int posX, int posY, int posOffset, int amount, int totalAmount,
				int thickness, int texOffsetX, int texOffsetY, double texWidth, double texHeight) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			if (vertical) {
				posY += totalAmount - posOffset;
				buffer.pos(posX, posY, 0).tex(texOffsetX / texWidth,
						texOffsetY / texHeight).endVertex();
				buffer.pos(posX + thickness, posY, 0).tex(texOffsetX / texWidth,
						(texOffsetY + thickness) / texHeight).endVertex();
				buffer.pos(posX + thickness, posY - amount, 0).tex((texOffsetX + amount) / texWidth,
						(texOffsetY + thickness) / texHeight).endVertex();
				buffer.pos(posX, posY - amount, 0).tex((texOffsetX + amount) / texWidth,
						texOffsetY / texHeight).endVertex();
			} else {
				posX += posOffset;
				buffer.pos(posX, posY + thickness, 0).tex(texOffsetX / texWidth,
						(texOffsetY + thickness) / texHeight).endVertex();
				buffer.pos(posX + amount, posY + thickness, 0).tex((texOffsetX + amount) / texWidth,
						(texOffsetY + thickness) / texHeight).endVertex();
				buffer.pos(posX + amount, posY, 0).tex((texOffsetX + amount) / texWidth,
						texOffsetY / texHeight).endVertex();
				buffer.pos(posX, posY, 0).tex(texOffsetX / texWidth,
						texOffsetY / texHeight).endVertex();
			}
			tessellator.draw();
		}
	}
}
