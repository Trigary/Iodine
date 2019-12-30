package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class IodineGui extends GuiBase {
	private GuiElement focused;
	
	public IodineGui(@NotNull IodineMod mod, int id) {
		super(mod, id);
	}
	
	
	
	@Override
	protected void deserializeStart(@NotNull ByteBuffer buffer) {}
	
	@Override
	protected void onElementRemoved(@NotNull GuiElement element) {
		if (focused == element) {
			focused = null;
		}
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected IntPair calculatePosition(int screenWidth, int screenHeight, int guiWidth, int guiHeight) {
		return new IntPair(screenWidth / 2 - guiWidth / 2, screenHeight / 2 - guiHeight / 2);
	}
	
	@Override
	protected void onUpdatedResolution() {
		if (focused != null) {
			focused = getElement(focused.getId());
			focused.setFocused(true); //can't be null, since onElementRemoved wasn't called
		}
	}
	
	
	
	public void onMousePressed(double mouseX, double mouseY) {
		for (GuiElement element : getAllElements()) {
			if (element.onMousePressed(mouseX, mouseY)) {
				if (focused != null) {
					focused.setFocused(false);
				}
				focused = element;
				focused.setFocused(true);
			}
		}
	}
	
	public void onMouseReleased(double mouseX, double mouseY) {
		if (focused != null) {
			focused.onMouseReleased(mouseX, mouseY);
		}
	}
	
	public void onMouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
		if (focused != null) {
			focused.onMouseDragged(mouseX, mouseY, deltaX, deltaY);
		}
	}
	
	public void onKeyPressed(int key, int scanCode, int modifiers) {
		if (focused != null) {
			focused.onKeyPressed(key, scanCode, modifiers);
		}
	}
	
	public void onKeyReleased(int key, int scanCode, int modifiers) {
		if (focused != null) {
			focused.onKeyReleased(key, scanCode, modifiers);
		}
	}
	
	public void onCharTyped(char codePoint, int modifiers) {
		if (focused != null) {
			focused.onCharTyped(codePoint, modifiers);
		}
	}
}
