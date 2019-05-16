package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.container.GuiParentPlus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link GuiElement}.
 */
public abstract class GuiElementImpl<T extends GuiElement<T>> implements GuiElement<T> {
	protected final IodineGuiImpl gui;
	private final GuiElementType type;
	private final int internalId;
	private final Object id;
	private GuiParentPlus<?> parent;
	
	protected GuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		this.gui = gui;
		this.internalId = internalId;
		this.id = id;
		this.type = type;
		parent = gui;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public IodineGui getGui() {
		return gui;
	}
	
	/**
	 * Gets the ID that is used by the backend to identify this element.
	 *
	 * @return the internal ID
	 */
	@Contract(pure = true)
	public int getInternalId() {
		return internalId;
	}
	
	@Contract(pure = true)
	@Override
	public Object getId() {
		return id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public GuiParentPlus<?> getParent() {
		return parent;
	}
	
	public void setParent(@NotNull GuiParentPlus<?> parent) {
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		this.parent = parent;
	}
	
	
	
	/**
	 * Serializes this element into the specified buffer.
	 * Implementations must call the superclass' implementation.
	 * Other than that, this method should only be called by {@link IodineGuiImpl}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	public void serialize(@NotNull ByteBuffer buffer) {
		buffer.put(type.getId());
		buffer.putInt(internalId);
	}
}
