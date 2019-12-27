package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.container.base.GuiBase;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.util.IntPair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class IodineGui extends GuiBase {
	private final Set<GuiElement> pressedElements = new HashSet<>();
	
	public IodineGui(@NotNull IodineMod mod, int id) {
		super(mod, id);
	}
	
	
	
	@Override
	protected final void deserializeStart(@NotNull ByteBuffer buffer) {}
	
	@Override
	protected final void onElementRemoved(@NotNull GuiElement element) {
		pressedElements.remove(element);
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected final IntPair calculatePosition(int screenWidth, int screenHeight, int guiWidth, int guiHeight) {
		return new IntPair(screenWidth / 2 - guiWidth / 2, screenHeight / 2 - guiHeight / 2);
	}
	
	
	
	public final void onEscapePressed() {
		getMod().getGui().playerCloseGui();
	}
	
	public final void onKeyTyped(char typedChar, int keyCode) {
		for (GuiElement element : getElements()) {
			element.onKeyTyped(typedChar, keyCode);
		}
	}
	
	public final void onMousePressed(int mouseX, int mouseY) {
		for (GuiElement element : getElements()) {
			if (element.onMousePressed(mouseX, mouseY)) {
				pressedElements.add(element);
			}
		}
	}
	
	public final void onMouseReleased(int mouseX, int mouseY) {
		for (GuiElement element : pressedElements) {
			element.onMouseReleased(mouseX, mouseY);
		}
		pressedElements.clear();
	}
}
