package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.gui.container.base.GuiParent;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class IodineRoot implements GuiParent {
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Collection<GuiElement> drawOrderedElements = new TreeSet<>(Comparator
			.comparing(GuiElement::getDrawPriority)
			.thenComparing(GuiElement::getId));
	private final IodineMod mod;
	private final int id;
	private Object attachment;
	private RootGuiContainer rootElement;
	
	protected IodineRoot(@NotNull IodineMod mod, int id) {
		this.mod = mod;
		this.id = id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final IodineMod getMod() {
		return mod;
	}
	
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	@Contract(pure = true)
	public final Object getAttachment() {
		return attachment;
	}
	
	public final void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final Collection<GuiElement> getAllElements() {
		return elements.values();
	}
	
	@NotNull
	@Contract(pure = true)
	public final GuiElement getElement(int id) {
		GuiElement element = elements.get(id);
		Validate.notNull(element, "ID must point to a valid element");
		return element;
	}
	
	
	
	public final void deserialize(@NotNull ByteBuffer buffer) {
		mod.getLogger().debug("Base > deserializing {}", id);
		deserializeStart(buffer);
		
		int removeCount = buffer.getInt();
		for (int i = 0; i < removeCount; i++) {
			GuiElement removed = elements.remove(buffer.getInt());
			mod.getLogger().debug("Base > removing {} in {}", removed.getId(), id);
			drawOrderedElements.remove(removed);
			onElementRemoved(removed);
		}
		
		while (buffer.hasRemaining()) {
			GuiElement changed = mod.getElementManager().getElement(this, elements, buffer);
			mod.getLogger().debug("Base > deserializing {} in {}", changed.getId(), id);
			drawOrderedElements.remove(changed);
			changed.deserialize(buffer);
			drawOrderedElements.add(changed);
		}
		
		if (rootElement == null) {
			rootElement = (RootGuiContainer) elements.get(0);
		}
		rootElement.initialize();
		update();
	}
	
	protected abstract void deserializeStart(@NotNull ByteBuffer buffer);
	
	protected abstract void onElementRemoved(@NotNull GuiElement element);
	
	
	
	public final void update() {
		mod.getLogger().debug("Base > updating {}", id);
		rootElement.calculateSize(mod.getScreenWidth(), mod.getScreenHeight());
		IntPair position = calculatePosition(mod.getScreenWidth(), mod.getScreenHeight(),
				rootElement.getTotalWidth(), rootElement.getTotalHeight());
		rootElement.setPosition(position.getX(), position.getY());
		for (GuiElement element : elements.values()) {
			element.update();
		}
		onUpdated();
	}
	
	@NotNull
	@Contract(pure = true)
	protected abstract IntPair calculatePosition(int screenWidth, int screenHeight, int guiWidth, int guiHeight);
	
	protected abstract void onUpdated();
	
	
	
	public final void draw(int mouseX, int mouseY, float partialTicks) {
		for (GuiElement element : drawOrderedElements) {
			element.draw(mouseX, mouseY, partialTicks);
		}
	}
}
