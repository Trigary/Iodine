package hu.trigary.iodine.bukkit.gui.element.base;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiParentPlus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link GuiElement}.
 */
public abstract class GuiElementImpl<T extends GuiElement<T>> implements GuiElement<T> {
	private final IodineGuiImpl gui;
	private final GuiElementType type;
	private final int internalId;
	private final Object id;
	private GuiParentPlus<?> parent;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the exact type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	protected GuiElementImpl(@NotNull IodineGuiImpl gui,
			@NotNull GuiElementType type, int internalId, @NotNull Object id) {
		this.gui = gui;
		this.internalId = internalId;
		this.id = id;
		this.type = type;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final IodineGuiImpl getGui() {
		return gui;
	}
	
	/**
	 * Gets the ID that is used by the backend to identify this element.
	 *
	 * @return the internal ID
	 */
	@Contract(pure = true)
	public final int getInternalId() {
		return internalId;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Object getId() {
		return id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final GuiParentPlus<?> getParent() {
		return parent;
	}
	
	/**
	 * Sets this element's parent,
	 * internally unregistering it from its previous parent.
	 *
	 * @param parent this element's new parent
	 */
	public final void setParent(@NotNull GuiParentPlus<?> parent) {
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		this.parent = parent;
	}
	
	
	
	/**
	 * Serializes this element into the specified buffer.
	 * This method should only be called by {@link IodineGuiImpl}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	public final void serialize(@NotNull ByteBuffer buffer) {
		buffer.put(type.getId());
		buffer.putInt(internalId);
		serializeImpl(buffer);
	}
	
	protected abstract void serializeImpl(@NotNull ByteBuffer buffer);
	
	/**
	 * Called by {@link hu.trigary.iodine.bukkit.network.handler.GuiChangePacketHandler}
	 * when a viewer acts upon this element.
	 * Malicious intent should be assumed, everything should be validated.
	 * Network delays and multiple viewers should be kept in mind.
	 * It should be assumed that callbacks change the GUI,
	 * so if both a callback and {@link IodineGuiImpl#flagAndUpdate(GuiElementImpl)}} are called,
	 * then {@link IodineGuiImpl#flagAndAtomicUpdate(GuiElementImpl, Runnable)} should be used.
	 *
	 * @param player the sender of the packet
	 * @param message the packet's contents
	 */
	public abstract void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message);
}
