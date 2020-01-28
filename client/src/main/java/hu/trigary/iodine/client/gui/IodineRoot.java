package hu.trigary.iodine.client.gui;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.container.RootGuiContainer;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.*;

/**
 * Represents a {@link IodineGui} or {@link IodineOverlay}.
 * Instances of this class are the top-level roots of GUIs/overlays.
 */
public abstract class IodineRoot implements Closeable {
	private final Map<Integer, GuiElement> elements = new HashMap<>();
	private final Collection<GuiElement> drawOrderedElements = new TreeSet<>(Comparator
			.comparing(GuiElement::getDrawPriority)
			.thenComparing(GuiElement::getId));
	private final IodineMod mod;
	private final int id;
	private RootGuiContainer rootElement;
	
	/**
	 * Creates a new instance.
	 *
	 * @param mod the mod instance
	 * @param id this instance's ID
	 */
	protected IodineRoot(@NotNull IodineMod mod, int id) {
		this.mod = mod;
		this.id = id;
	}
	
	
	
	/**
	 * Gets the mod instance.
	 *
	 * @return the mod instance
	 */
	@NotNull
	@Contract(pure = true)
	public final IodineMod getMod() {
		return mod;
	}
	
	/**
	 * Gets this instance's ID.
	 *
	 * @return the ID
	 */
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	
	
	/**
	 * Gets all elements that are contained in this instance.
	 *
	 * @return all child elements
	 */
	@NotNull
	@Contract(pure = true)
	public final Collection<GuiElement> getAllElements() {
		return elements.values();
	}
	
	/**
	 * Gets the element with the specified internal ID.
	 * The element must exist.
	 *
	 * @param id the ID to search for
	 * @return the found element
	 */
	@NotNull
	@Contract(pure = true)
	public final GuiElement getElement(int id) {
		GuiElement element = elements.get(id);
		Validate.notNull(element, "ID must point to a valid element");
		return element;
	}
	
	
	
	/**
	 * Updates this instance from the specified buffer.
	 *
	 * @param buffer the buffer the data is stored in
	 */
	public final void deserialize(@NotNull InputBuffer buffer) {
		mod.getLogger().debug("Base > deserializing {}", id);
		deserializeStart(buffer);
		
		int removeCount = buffer.readInt();
		for (int i = 0; i < removeCount; i++) {
			try (GuiElement removed = elements.remove(buffer.readInt())) {
				mod.getLogger().debug("Base > removing {} in {}", removed.getId(), id);
				drawOrderedElements.remove(removed);
				onElementRemoved(removed);
			}
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
	
	/**
	 * This method deserializes type-specific data from the buffer
	 * before the elements' deserialization starts.
	 *
	 * @param buffer the buffer the data is stored in
	 */
	protected abstract void deserializeStart(@NotNull InputBuffer buffer);
	
	/**
	 * Called when an element was removed from this instance.
	 * Can be used for eg. cleanup operations.
	 *
	 * @param element the removed element
	 */
	protected abstract void onElementRemoved(@NotNull GuiElement element);
	
	
	
	/**
	 * Called after {@link #deserialize(InputBuffer)} and when the client resolution changed.
	 * Updates the contained elements' sizes and positions.
	 */
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
	
	/**
	 * Calculates this instance's position based on the screen and gui size.
	 *
	 * @param screenWidth the screen's width
	 * @param screenHeight the screen's height
	 * @param width this instance's width
	 * @param height this instance's height
	 * @return the calculated position for this instance
	 */
	@NotNull
	@Contract(pure = true)
	protected abstract IntPair calculatePosition(int screenWidth, int screenHeight, int width, int height);
	
	/**
	 * Called inside {@link #update()} after all contained elements have been updated.
	 * Can be used to eg. set the focused element.
	 */
	protected abstract void onUpdated();
	
	
	
	/**
	 * Renders each element on the screen.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param partialTicks the client's partial ticks, used for animations
	 */
	public final void draw(int mouseX, int mouseY, float partialTicks) {
		for (GuiElement element : drawOrderedElements) {
			element.draw(mouseX, mouseY, partialTicks);
		}
	}
	
	
	
	/**
	 * Clears internal data, eg. the closing of {@link java.io.Closeable}.
	 * Must be called exactly once per instance and always after {@link #update()}.
	 */
	@Override
	public final void close() {
		for (GuiElement element : elements.values()) {
			element.close();
		}
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getId();
	}
}
