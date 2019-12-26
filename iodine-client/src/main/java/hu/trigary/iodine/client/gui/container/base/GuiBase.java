package hu.trigary.iodine.client.gui.container.base;

import hu.trigary.iodine.client.IodineModBase;
import hu.trigary.iodine.client.util.Validator;
import hu.trigary.iodine.client.util.IntPair;
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
	
	@NotNull
	@Contract(pure = true)
	public final Collection<GuiElement> getElements() {
		return elements.values();
	}
	
	@NotNull
	@Contract(pure = true)
	public final GuiElement getElement(int id) {
		GuiElement element = elements.get(id);
		Validator.notNull(element, "ID must point to a valid element");
		return element;
	}
	
	
	
	public final void deserialize(@NotNull ByteBuffer buffer) {
		deserializeStart(buffer);
		
		int removeCount = buffer.getInt();
		for (int i = 0; i < removeCount; i++) {
			GuiElement removed = elements.remove(buffer.getInt());
			drawOrderedElements.remove(removed);
			onElementRemoved(removed);
		}
		
		while (buffer.hasRemaining()) {
			GuiElement changed = mod.getElement().getElement(this, elements, buffer);
			drawOrderedElements.remove(changed);
			changed.deserialize(buffer);
			drawOrderedElements.add(changed);
		}
		
		if (rootElement == null) {
			rootElement = (RootGuiContainer) elements.get(0);
		}
		rootElement.initialize();
		updateResolution();
	}
	
	protected abstract void deserializeStart(@NotNull ByteBuffer buffer);
	
	protected void onElementRemoved(@NotNull GuiElement element) {}
	
	
	
	public final void updateResolution() {
		IntPair screenSize = mod.getScreenSize();
		rootElement.calculateSize(screenSize.getX(), screenSize.getY());
		IntPair position = calculatePosition(screenSize.getX(), screenSize.getY(),
				rootElement.getWidth(), rootElement.getHeight());
		rootElement.setPosition(position.getX(), position.getY());
		for (GuiElement element : elements.values()) {
			element.update();
		}
	}
	
	@NotNull
	@Contract(pure = true)
	protected abstract IntPair calculatePosition(int screenWidth, int screenHeight, int guiWidth, int guiHeight);
	
	
	
	public void draw(int mouseX, int mouseY, float partialTicks) {
		for (GuiElement element : drawOrderedElements) {
			element.draw(mouseX, mouseY, partialTicks);
		}
	}
}
