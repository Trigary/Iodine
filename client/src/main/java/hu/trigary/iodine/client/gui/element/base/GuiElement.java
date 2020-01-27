package hu.trigary.iodine.client.gui.element.base;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.IodineRoot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * The base class for GUI elements.
 * All other elements extend this class.
 */
public abstract class GuiElement implements Closeable {
	private final short[] padding = new short[4];
	private final IodineRoot root;
	private final int id;
	private byte drawPriority;
	private int elementWidth;
	private int elementHeight;
	private int totalWidth;
	private int totalHeight;
	private int positionX;
	private int positionY;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected GuiElement(@NotNull IodineRoot root, int id) {
		this.root = root;
		this.id = id;
	}
	
	
	
	/**
	 * Gets the GUI or overlay that contains this element.
	 *
	 * @return the instance that contains this element
	 */
	@NotNull
	@Contract(pure = true)
	public final IodineRoot getRoot() {
		return root;
	}
	
	/**
	 * Gets the internal ID of this element element.
	 *
	 * @return this element's internal ID
	 */
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	/**
	 * Gets this element's draw priority.
	 * Should be used together with {@link #getId()} to order the elements.
	 *
	 * @return the draw priority of this element
	 */
	@Contract(pure = true)
	public final byte getDrawPriority() {
		return drawPriority;
	}
	
	/**
	 * Gets this element's width, paddings included.
	 *
	 * @return this element's width together with its paddings
	 */
	@Contract(pure = true)
	public final int getTotalWidth() {
		return totalWidth;
	}
	
	/**
	 * Gets this element's height, paddings included.
	 *
	 * @return this element's height together with its paddings
	 */
	@Contract(pure = true)
	public final int getTotalHeight() {
		return totalHeight;
	}
	
	
	
	/**
	 * Loads this element from the specified buffer.
	 * Should only be called by {@link IodineRoot}.
	 *
	 * @param buffer the buffer the data is stored in
	 */
	public final void deserialize(@NotNull ByteBuffer buffer) {
		padding[0] = buffer.getShort();
		padding[1] = buffer.getShort();
		padding[2] = buffer.getShort();
		padding[3] = buffer.getShort();
		drawPriority = buffer.get();
		deserializeImpl(buffer);
	}
	
	/**
	 * Loads this element's type specified data from the specific buffer.
	 * This method should only be called by {@link #deserialize(ByteBuffer)}.
	 *
	 * @param buffer the buffer the data is stored in
	 */
	protected abstract void deserializeImpl(@NotNull ByteBuffer buffer);
	
	/**
	 * Called directly after {@link #deserialize(ByteBuffer)} has been called on each changed element.
	 * This element in particular might not have received that method call, that isn't known.
	 * This method is called in a top-down order: parents call this method on all of their children.
	 * It can be used to convert number-based IDs to object references, set up element relations, etc.
	 */
	public void initialize() {}
	
	/**
	 * Calculates the size of this element, with paddings included.
	 * Called directly after each element has received the {@link #initialize()}
	 * call and when the client resolution changes.
	 * This method is called in a bottom-up order: parents first call
	 * this on all their children before returning a value themselves.
	 *
	 * @param screenWidth the width of the screen
	 * @param screenHeight the height of the screen
	 */
	public final void calculateSize(int screenWidth, int screenHeight) { //in case percentage based stuff is added
		IntPair size = calculateSizeImpl(screenWidth, screenHeight);
		elementWidth = size.getX();
		elementHeight = size.getY();
		totalWidth = elementWidth + padding[2] + padding[3];
		totalHeight = elementHeight + padding[0] + padding[1];
	}
	
	/**
	 * Calculates the size of this elements, without paddings.
	 * Should only be called by {@link #calculateSize(int, int)}.
	 *
	 * @param screenWidth the width of the screen
	 * @param screenHeight the height of the screen
	 * @return the calculated size
	 */
	@NotNull
	protected abstract IntPair calculateSizeImpl(int screenWidth, int screenHeight);
	
	/**
	 * Sets this element's position, internally handling the paddings.
	 * Called directly after {@link #calculateSize(int, int)} has been called on each element.
	 * This method is called in a top-down order: parents call this method on all of their children.
	 *
	 * @param x this element's new X position
	 * @param y this element's new Y position
	 */
	public final void setPosition(int x, int y) {
		positionX = x + padding[2];
		positionY = y + padding[0];
		setChildrenPositions(positionX, positionY);
	}
	
	/**
	 * Sets this element's children's positions.
	 * Should only be called be {@link #setPosition(int, int)}.
	 *
	 * @param offsetX the minimum value of children X positions
	 * @param offsetY the minimum value of children Y positions
	 */
	protected void setChildrenPositions(int offsetX, int offsetY) {}
	
	/**
	 * Allows for internal data recalculation for the possibly changed size and position.
	 * Called directly after each element has received the {@link #setPosition(int, int)} call.
	 * The order in which this method is called on elements is unspecified.
	 */
	public final void update() {
		updateImpl(positionX, positionY, elementWidth, elementHeight);
	}
	
	/**
	 * Allows for internal data recalculation for the possibly changed size and position.
	 * Should only be called by {@link #update()}.
	 *
	 * @param positionX this element's X position, with paddings included
	 * @param positionY this element's Y position, with paddings included
	 * @param width this element's width, with paddings excluded
	 * @param height this element's height, with paddings excluded
	 */
	protected abstract void updateImpl(int positionX, int positionY, int width, int height);
	
	
	
	/**
	 * Renders this element on the screen.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param partialTicks the client's partial ticks, used for animations
	 */
	public final void draw(int mouseX, int mouseY, float partialTicks) {
		drawImpl(positionX, positionY, elementWidth, elementHeight, mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Renders this element on the screen.
	 * Should only be called be {@link #draw(int, int, float)}.
	 *
	 * @param positionX this element's X position, with paddings included
	 * @param positionY this element's Y position, with paddings included
	 * @param width this element's width, with paddings excluded
	 * @param height this element's height, with paddings excluded
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param partialTicks the client's partial ticks, used for animations
	 */
	protected abstract void drawImpl(int width, int height, int positionX,
			int positionY, int mouseX, int mouseY, float partialTicks);
	
	/**
	 * Sends a {@link PacketType#CLIENT_GUI_CLOSE} packet with
	 * the GUI's and the element's ID preceding the specified contents.
	 *
	 * @param dataLength the length of the data specified in the callback
	 * @param dataProvider a callback that puts the data in the buffer
	 */
	protected final void sendChangePacket(int dataLength, @NotNull Consumer<ByteBuffer> dataProvider) {
		getRoot().getMod().getLogger().debug("Root > {} sending change packet in {}", id, root.getId());
		getRoot().getMod().getNetworkManager().send(PacketType.CLIENT_GUI_CHANGE, dataLength + 8, buffer -> {
			buffer.putInt(root.getId());
			buffer.putInt(id);
			dataProvider.accept(buffer);
		});
	}
	
	
	
	/**
	 * Called when this element became focused or lost its focused status.
	 * This can be due to mouse clicks or GUI updating (deserialization, etc).
	 *
	 * @param focused whether this element is now focused or not
	 */
	public void setFocused(boolean focused) {}
	
	/**
	 * Called when the left mouse button is pressed anywhere in the window.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @return whether this element was clicked
	 */
	public boolean onMousePressed(double mouseX, double mouseY) {
		return false;
	}
	
	/**
	 * Called when the left mouse button is released after being pressed.
	 * This method is only called on elements that are focused.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 */
	public void onMouseReleased(double mouseX, double mouseY) {}
	
	/**
	 * Called when the left mouse button is released after being pressed and moved around.
	 * This method is only called on elements that are focused.
	 *
	 * @param mouseX the mouse's X position
	 * @param mouseY the mouse's Y position
	 * @param deltaX the mouse's position's delta X
	 * @param deltaY the mouse's position's delta Y
	 */
	public void onMouseDragged(double mouseX, double mouseY, double deltaX, double deltaY) {}
	
	/**
	 * Called when a key is pressed.
	 * This method is only called on elements that are focused.
	 *
	 * @param key the pressed key
	 * @param scanCode the key's scan code
	 * @param modifiers the modifiers active when the key press happened
	 */
	public void onKeyPressed(int key, int scanCode, int modifiers) {}
	
	/**
	 * Called when a key is released.
	 * This method is only called on elements that are focused.
	 *
	 * @param key the released key
	 * @param scanCode the key's scan code
	 * @param modifiers the modifiers active when the key release happened
	 */
	public void onKeyReleased(int key, int scanCode, int modifiers) {}
	
	/**
	 * Called when a character is typed.
	 * This method is only called on elements that are focused.
	 *
	 * @param codePoint the typed character
	 * @param modifiers the modifiers active when the key press happened
	 */
	public void onCharTyped(char codePoint, int modifiers) {}
	
	
	
	/**
	 * Allows for internal data to be cleared, eg. the closing of {@link java.io.Closeable}.
	 * Called exactly once per instance and always after {@link #update()}.
	 */
	@Override
	public void close() {}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getId();
	}
}
