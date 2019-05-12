package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.element.GuiElement;
import hu.trigary.iodine.forge.network.out.ClientGuiClosePacket;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class IodineGui extends GuiScreen {
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final IodineMod mod;
	private final int id;
	
	public IodineGui(@NotNull IodineMod mod, int id, @NotNull byte[] data) {
		this.mod = mod;
		this.id = id;
		deserialize(data);
	}
	
	
	
	public void deserialize(@NotNull byte[] data) {
		mod.getGui().deserializeElements(this, elements, ByteBuffer.wrap(data));
	}
	
	
	
	@Override
	public void initGui() {
	
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		for (GuiElement element : elements.values()) {
			element.draw(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void onGuiClosed() {
	
	}
	
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			mod.getNetwork().send(new ClientGuiClosePacket(id));
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
	
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	
	}
}
