package hu.trigary.iodine.bukkit.gui.element.base;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link GuiElement}.
 *
 * @param <T> the class implementing this abstract class
 */
public abstract class GuiElementImpl<T extends GuiElement<T>> implements GuiElement<T> {
	private final GuiBaseImpl<?> gui;
	private final GuiElementType type;
	private final short internalId;
	private final Object id;
	private Object attachment;
	private GuiParentPlus<?> parent;
	private short drawPriority;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param type the exact type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	protected GuiElementImpl(@NotNull GuiBaseImpl<?> gui, @NotNull GuiElementType type, short internalId, @NotNull Object id) {
		this.gui = gui;
		this.internalId = internalId;
		this.id = id;
		this.type = type;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final GuiBaseImpl<?> getGui() {
		return gui;
	}
	
	/**
	 * Gets the ID that is used by the backend to identify this element.
	 *
	 * @return the internal ID
	 */
	@Contract(pure = true)
	public final short getInternalId() {
		return internalId;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Object getId() {
		return id;
	}
	
	@Override
	public void setAttachment(@Nullable Object attachment) {
		this.attachment = attachment;
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public Object getAttachment() {
		return attachment;
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
	
	
	
	@Override
	@Contract(pure = true)
	public short getDrawPriority() {
		return drawPriority;
	}
	
	@Override
	public void setDrawPriority(short priority) {
		drawPriority = priority; //TODO client-side: (priority << 16) | internalId
	}
	
	
	
	/**
	 * Serializes this element into the specified buffer.
	 * This method should only be called by {@link IodineGuiImpl}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	public final void serialize(@NotNull ResizingByteBuffer buffer) {
		buffer.putByte(type.getId());
		buffer.putShort(internalId);
		buffer.putShort(drawPriority);
		serializeImpl(buffer);
	}
	
	protected abstract void serializeImpl(@NotNull ResizingByteBuffer buffer);
	
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
