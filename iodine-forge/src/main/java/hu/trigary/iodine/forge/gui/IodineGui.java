package hu.trigary.iodine.forge.gui;

import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.gui.container.RootGuiContainer;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import hu.trigary.iodine.forge.network.out.ClientGuiClosePacket;
import net.minecraft.client.gui.GuiScreen;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public class IodineGui extends GuiScreen {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 200;
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Set<GuiElement> pressedElements = new HashSet<>();
	private final IodineMod mod;
	private final int id;
	private RootGuiContainer rootElement;
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
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		int removeCount = buffer.getInt();
		for (int i = 0; i < removeCount; i++) {
			elements.remove(buffer.getInt());
		}
		
		while (buffer.hasRemaining()) {
			mod.getGui().deserializeElement(this, elements, buffer);
		}
		
		if (rootElement == null) {
			rootElement = (RootGuiContainer) elements.get(0);
		}
		rootElement.initialize(null, 0, 0);
		
		if (left != -1) {
			initGui();
		}
		
		//TODO GuiElement call order docs:
		// constructor
		// deserialize
		// initialize (used for stuff that requires all elements to be deserialized)
		// update (can be called multiple times for the same instance, see line below)
		//after server change packet, on resolution change: update
	}
	
	
	
	@Override
	public void initGui() {
		left = width / 2 - WIDTH / 2;
		top = height / 2 - HEIGHT / 2;
		rootElement.onResolutionChanged(left, top);
		elements.values().forEach(GuiElement::update);
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
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 1) {
			mod.getNetwork().send(new ClientGuiClosePacket(id));
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) {
				mc.setIngameFocus();
			}
		} else {
			for (GuiElement element : elements.values()) {
				element.onKeyTyped(typedChar, keyCode);
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			for (GuiElement element : elements.values()) {
				if (element.onMousePressed(mouseX, mouseY)) {
					pressedElements.add(element);
				}
			}
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 0) {
			for (GuiElement element : pressedElements) {
				element.onMouseReleased(mouseX, mouseY);
			}
			pressedElements.clear();
		}
	}
}
