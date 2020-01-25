package hu.trigary.iodine.forge.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.RadioButtonGuiElement;
import hu.trigary.iodine.forge.gui.IodineGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link RadioButtonGuiElement}.
 */
public class RadioButtonGuiElementImpl extends RadioButtonGuiElement {
	private static final ResourceLocation TEXTURE = new ResourceLocation("iodine", "radio-button.png");
	private RadioButton widget;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public RadioButtonGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		widget = new RadioButton(positionX, positionY, width, height, checked);
		widget.active = editable;
	}
	
	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {
		widget.render(mouseX, mouseY, partialTicks);
		if (widget.isHovered()) {
			IodineGuiScreen.renderTooltip(mouseX, mouseY, tooltip);
		}
	}
	
	
	
	@Override
	public boolean onMousePressed(double mouseX, double mouseY) {
		return widget.mouseClicked(mouseX, mouseY, 0);
	}
	
	
	
	private class RadioButton extends AbstractButton {
		private final boolean checked;
		
		RadioButton(int x, int y, int width, int height, boolean checked) {
			super(x, y, width, height, "");
			this.checked = checked;
		}
		
		@Override
		public void onPress() {
			onChanged();
		}
		
		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			//noinspection resource
			Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
			GlStateManager.color4f(1, 1, 1, alpha);
			blit(x, y, checked ? 32 : 0, 0, width, height, 64, 32);
		}
	}
}
