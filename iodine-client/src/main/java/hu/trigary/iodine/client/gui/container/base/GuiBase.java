package hu.trigary.iodine.client.gui.container.base;

import hu.trigary.iodine.client.IodineModBase;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class GuiBase implements GuiParent {
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Collection<GuiElement> drawOrderedElements = new TreeSet<>(Comparator
			.comparing(GuiElement::getDrawPriority)
			.thenComparing(GuiElement::getId));
	private final Set<GuiElement> pressedElements = new HashSet<>();
	private final IodineModBase mod;
	private final int id;
	private RootGuiContainer rootElement;
	
	protected GuiBase(@NotNull IodineModBase mod, int id) {
		this.mod = mod;
		this.id = id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final IodineModBase getMod() {
		return mod;
	}
	
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	
	
	public final void deserialize(@NotNull ByteBuffer buffer) {
		deserializeStart(buffer);
		
		int removeCount = buffer.getInt();
		for (int i = 0; i < removeCount; i++) {
			GuiElement removed = elements.remove(buffer.getInt());
			drawOrderedElements.remove(removed);
			pressedElements.remove(removed);
		}
		
		while (buffer.hasRemaining()) {
			GuiElement changed = mod.getGui().deserializeElement(this, elements, buffer);
			drawOrderedElements.add(changed);
		}
		
		if (rootElement == null) {
			rootElement = (RootGuiContainer) elements.get(0);
		}
		rootElement.initialize();
	}
	
	protected abstract void deserializeStart(@NotNull ByteBuffer buffer);
	
	
	
	public final void onResolutionChanged(int screenWidth, int screenHeight) {
		rootElement.calculateSize(screenWidth, screenHeight);
		int[] position = calculatePosition();
		rootElement.setPosition(position[0], position[1]);
		for (GuiElement element : elements.values()) {
			element.update();
		}
	}
	
	@NotNull
	protected abstract int[] calculatePosition();
	
	
	
	public void draw(int mouseX, int mouseY, float partialTicks) {
		for (GuiElement element : drawOrderedElements) {
			element.draw(mouseX, mouseY, partialTicks);
		}
	}
}
