package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.container.base.GuiParent;
import hu.trigary.iodine.forge.gui.element.base.ClickableElement;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import hu.trigary.iodine.forge.network.out.ClientGuiClosePacket;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IodineGui extends GuiScreen implements GuiParent {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 200;
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Map<GuiElement, IntPair> children = new HashMap<>();
	private final Map<GuiElement, IntPair> clickables = new HashMap<>();
	private final IodineMod mod;
	private final int id;
	private int left = -1;
	private int top = -1;
	
	public IodineGui(@NotNull IodineMod mod, int id, @NotNull byte[] data) {
		this.mod = mod;
		this.id = id;
		deserialize(data);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public IodineMod getMod() {
		return mod;
	}
	
	@Contract(pure = true)
	public int getId() {
		return id;
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
	
	
	
	public void deserialize(@NotNull byte[] data) {
		elements.clear();
		children.clear();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		int elementCount = buffer.getInt();
		for (int i = 0; i < elementCount; i++) {
			mod.getGui().deserializeElement(this, elements, buffer);
		}
		
		while (buffer.hasRemaining()) {
			children.put(elements.get(buffer.getInt()), new IntPair(buffer.getShort(), buffer.getShort()));
		}
		
		children.forEach((child, position) -> child.initialize(this, position.x, position.y));
		if (left != -1) {
			initGui();
		}
		
		//TODO GuiElement call order docs:
		// constructor, deserialize
		// initialize (used for stuff that requires all elements to be deserialized)
		// update (can be called multiple times for the same instance)
		//after server change packet:
		// deserialize -> initGui
	}
	
	public void registerClickable(@NotNull ClickableElement element, int width, int height) {
		clickables.put((GuiElement) element, new IntPair(width, height));
	}
	
	
	
	@Override
	public void initGui() {
		left = width / 2 - WIDTH / 2;
		top = height / 2 - HEIGHT / 2;
		elements.values().forEach(GuiElement::update);
	}
	
	@Override
	public int getLeft() {
		return left;
	}
	
	@Override
	public int getTop() {
		return top;
	}
	
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawRect(left, top, left + WIDTH, top + HEIGHT, 0xFFC6C6C6);
		
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
		if (mouseButton != 0) {
			return;
		}
		clickables.entrySet().stream()
				.filter(entry -> {
					int x = entry.getKey().getX();
					int y = entry.getKey().getY();
					IntPair box = entry.getValue();
					return mouseX >= x && mouseY > y && mouseX < x + box.x && mouseY < y + box.y;
				})
				.findFirst()
				.ifPresent(entry -> {
					((ClickableElement) entry.getKey()).onClicked();
					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				});
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		//for slider
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		//for slider
	}
	
	
	
	private static class IntPair {
		final int x;
		final int y;
		
		IntPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
