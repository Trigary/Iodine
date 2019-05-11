package hu.trigary.iodine.forge.ui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.ClientGuiClosePacket;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class IodineGui extends GuiScreen {
	private final IodineMod mod;
	private final int id;
	private boolean serverClosing;
	
	public IodineGui(@NotNull IodineMod mod, int id) {
		this.mod = mod;
		this.id = id;
	}
	
	
	
	public int getId() {
		return id;
	}
	
	
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		if (!serverClosing) {
			mod.getNetwork().send(new ClientGuiClosePacket(id));
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
	}
	
	
	
	public void flagServerClosing() {
		serverClosing = true;
	}
}
