package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import hu.trigary.iodine.client.IntPair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a GUI.
 */
public final class IodineGui extends IodineRoot {
	private GuiElement focused;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiManager}.
	 *
	 * @param mod the mod instance
	 * @param id this instance's ID
	 */
	public IodineGui(@NotNull IodineMod mod, int id) {
		super(mod, id);
	}
	
	
	
	@Override
	protected void deserializeStart(@NotNull InputBuffer buffer) {}
	
	@Override
	protected void onElementRemoved(@NotNull GuiElement element) {
		if (focused == element) {
			focused = null;
		}
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected IntPair calculatePosition(int screenWidth, int screenHeight, int width, int height) {
		return new IntPair(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2);
	}
	
	@Override
	protected void onUpdated() {
		if (focused != null) {
			focused = getElement(focused.getId());
			focused.setFocused(true); //can't be null, since onElementRemoved wasn't called
		}
	}
	
	
	
	/**
	 * Should be called when the client presses the left mouse button.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 */
	public void onMousePressed(double mouseX, double mouseY) {
		boolean gainedFocus = false;
		for (GuiElement element : getAllElements()) {
			if (element.onMousePressed(mouseX, mouseY)) {
				if (focused != null) {
					focused.setFocused(false);
				}
				focused = element;
				focused.setFocused(true);
				gainedFocus = true;
			}
		}
		if (!gainedFocus && focused != null) {
			focused.setFocused(false);
			focused = null;
		}
	}
	
	/**
	 * Should be called when the client releases the left mouse button.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 */
	public void onMouseReleased(double mouseX, double mouseY) {
		if (focused != null) {
			focused.onMouseReleased(mouseX, mouseY);
		}
	}
	
	/**
	 * Should be called when the client releases the left mouse button after moving the mouse around.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param deltaX the mouse's position's delta X
	 * @param deltaY the mouse's position's delta Y
	 */
	public void onMouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {
		if (focused != null) {
			focused.onMouseDragged(mouseX, mouseY, deltaX, deltaY);
		}
	}
	
	/**
	 * Should be called when the client presses a key.
	 *
	 * @param key the released key
	 * @param scanCode the key's scan code
	 * @param modifiers the modifiers active when the key release happened
	 */
	public void onKeyPressed(int key, int scanCode, int modifiers) {
		if (focused != null) {
			focused.onKeyPressed(key, scanCode, modifiers);
		}
	}
	
	/**
	 * Should be called when the client releases a key.
	 *
	 * @param key the released key
	 * @param scanCode the key's scan code
	 * @param modifiers the modifiers active when the key release happened
	 */
	public void onKeyReleased(int key, int scanCode, int modifiers) {
		if (focused != null) {
			focused.onKeyReleased(key, scanCode, modifiers);
		}
	}
	
	/**
	 * Should be called when the client types a character.
	 *
	 * @param codePoint the typed character
	 * @param modifiers the modifiers active when the key press happened
	 */
	public void onCharTyped(char codePoint, int modifiers) {
		if (focused != null) {
			focused.onCharTyped(codePoint, modifiers);
		}
	}
}
