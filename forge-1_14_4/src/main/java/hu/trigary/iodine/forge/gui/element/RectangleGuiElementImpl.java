package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.RectangleGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiUtils;
import net.minecraft.client.gui.AbstractGui;
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
	protected void updateImpl(int positionX, int positionY, int width, int height) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	@Override
	protected void drawImpl(int positionX, int positionY, int width, int height, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color4f(1, 1, 1, 1);
		AbstractGui.fill(positionX, positionY, positionX + width, positionY + height, color);
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
