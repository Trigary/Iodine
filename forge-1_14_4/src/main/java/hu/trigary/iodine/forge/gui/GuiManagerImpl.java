package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.IodineGui;
import hu.trigary.iodine.forge.IodineModImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link GuiManager}.
 */
public class GuiManagerImpl extends GuiManager {
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
	public GuiManagerImpl(@NotNull IodineModImpl mod) {
		super(mod);
	}



	@Override
	protected void openGuiImpl(@NotNull IodineGui gui) {
		Minecraft.getInstance().displayGuiScreen(new IodineGuiScreen(gui));
	}
	
	@Override
	protected void closeGuiImpl(@NotNull IodineGui gui, boolean byPlayer) {
		if (!byPlayer) {
			Minecraft minecraft = Minecraft.getInstance();
			Screen screen = minecraft.currentScreen;
			if (screen instanceof IodineGuiScreen && ((IodineGuiScreen) screen).getGui() == gui) {
				minecraft.displayGuiScreen(null);
			}
		}
	}
}
