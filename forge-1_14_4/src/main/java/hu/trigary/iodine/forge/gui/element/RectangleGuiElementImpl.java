package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.RectangleGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiScreen;
import net.minecraft.client.gui.AbstractGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link RectangleGuiElement}.
 */
public class RectangleGuiElementImpl extends RectangleGuiElement {
	private int positionX;
	private int positionY;

	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public RectangleGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		//TODO are these OpenGL calls necessary?
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		AbstractGui.fill(positionX, positionY, positionX + width, positionY + height, color);
		if (isInside(mouseX, mouseY)) {
			IodineGuiScreen.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		if (isInside(mouseX, mouseY)) {
			onChanged();
			return true;
		}
		return false;
	}
	
	
	
	@Contract(pure = true)
	private boolean isInside(double mouseX, double mouseY) {
		return mouseX >= positionX && mouseY >= positionY && mouseX < positionX + width && mouseY < positionY + height;
	}
}
