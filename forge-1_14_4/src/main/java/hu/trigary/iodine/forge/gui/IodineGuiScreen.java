package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.gui.IodineGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper that connects {@link Screen} and {@link IodineGui}.
 */
public class IodineGuiScreen extends Screen {
	private final IodineGui gui;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiManagerImpl}.
	 *
	 * @param gui the gui instance this instance wraps
	 */
	public IodineGuiScreen(@NotNull IodineGui gui) {
		super(new StringTextComponent("IodineGUI"));
		this.gui = gui;
	}
	
	
	
	/**
	 * Gets the gui instance this instance wraps.
	 *
	 * @return the gui instance
	 */
	public IodineGui getGui() {
		return gui;
	}
	
	
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void resize(@NotNull Minecraft minecraft, int screenWidth, int screenHeight) {
		setSize(screenWidth, screenHeight);
		gui.update();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		gui.draw(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void removed() {
		gui.getMod().getGuiManager().playerCloseGui();
	}
	
	
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			gui.onMousePressed(mouseX, mouseY);
		}
		return true;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			gui.onMouseReleased(mouseX, mouseY);
		}
		return true;
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (button == 0) {
			gui.onMouseDragged(mouseX, mouseY, deltaX, deltaY);
		}
		return true;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double value) {
		return true;
	}
	
	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (key == 256) {
			Minecraft.getInstance().displayGuiScreen(null);
			//TODO also close on inventory button maybe?
		} else {
			gui.onKeyPressed(key, scanCode, modifiers);
		}
		return true;
	}
	
	@Override
	public boolean keyReleased(int key, int scanCode, int modifiers) {
		gui.onKeyReleased(key, scanCode, modifiers);
		return true;
	}
	
	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		gui.onCharTyped(codePoint, modifiers);
		return true;
	}
}
