package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.GuiManager;
import hu.trigary.iodine.client.gui.IodineGui;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class GuiManagerImpl extends GuiManager {
	public GuiManagerImpl(@NotNull IodineMod mod) {
		super(mod);
	}
	
	
	
	@Override
	protected void openGuiImpl(@NotNull IodineGui gui) {
		throw new NotImplementedException(""); //TODO implement
	}
	
	@Override
	protected void closeGuiImpl(@NotNull IodineGui gui) {
		throw new NotImplementedException(""); //TODO implement
	}
}
