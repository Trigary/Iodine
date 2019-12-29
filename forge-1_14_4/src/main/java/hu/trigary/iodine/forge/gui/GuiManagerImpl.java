package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.IodineGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;

public class GuiManagerImpl extends GuiManager {
	public GuiManagerImpl(@NotNull IodineMod mod) {
		super(mod);
	}
	
	
	
	@Override
	protected void openGuiImpl(@NotNull IodineGui gui) {
		IodineGuiScreen screen = new IodineGuiScreen(gui);
		gui.setAttachment(screen);
		//noinspection resource
		Minecraft.getInstance().displayGuiScreen(screen);
	}
	
	@Override
	protected void closeGuiImpl(@NotNull IodineGui gui, boolean byPlayer) {
		if (!byPlayer) {
			//noinspection resource
			Minecraft minecraft = Minecraft.getInstance();
			Screen screen = minecraft.currentScreen;
			if (screen == gui.getAttachment()) {
				minecraft.displayGuiScreen(null);
			}
		}
	}
}
