package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.element.GuiElement;
import hu.trigary.iodine.forge.network.out.ClientGuiClosePacket;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IodineGui extends GuiScreen {
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Map<GuiElement, Position> children = new HashMap<>();
	private final IodineMod mod;
	private final int id;
	
	public IodineGui(@NotNull IodineMod mod, int id, @NotNull byte[] data) {
		this.mod = mod;
		this.id = id;
		deserialize(data);
	}
	
	
	
	public void deserialize(@NotNull byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		int elementCount = buffer.getInt();
		for (int i = 0; i < elementCount; i++) {
			mod.getGui().deserializeElement(this, elements, buffer);
		}
		
		for (GuiElement element : elements.values()) {
			element.resolveElementReferences();
		}
		
		while (buffer.hasRemaining()) {
			children.put(elements.get(buffer.getInt()), new Position(buffer.getShort(), buffer.getShort()));
		}
	}
	
	@NotNull
	@Contract(pure = true)
	public Collection<GuiElement> getElements() {
		return Collections.unmodifiableCollection(elements.values());
	}
	
	@NotNull
	@Contract(pure = true)
	public GuiElement getElement(int id) {
		GuiElement element = elements.get(id);
		Validate.notNull(element, "Valid element ID must be provided: no elements found with ID");
		return element;
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
	
	
	
	private static class Position {
		final short x;
		final short y;
		
		Position(short x, short y) {
			this.x = x;
			this.y = y;
		}
	}
}
