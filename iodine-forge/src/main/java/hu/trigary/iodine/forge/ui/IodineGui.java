package hu.trigary.iodine.forge.ui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.out.ClientGuiClosePacket;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class IodineGui extends GuiScreen {
	private final IodineMod mod;
	private final int id;
	
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
		
		//TODO this probably also fires when the server tells me to close it
		mod.getNetwork().send(new ClientGuiClosePacket(id));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
	}
}
