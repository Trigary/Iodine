package hu.trigary.iodine.server.gui.element.base;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.server.network.handler.GuiChangePacketHandler;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link GuiElement}.
 *
 * @param <T> the class implementing this abstract class
 */
public abstract class GuiElementImpl<T extends GuiElement<T>> implements GuiElement<T> {
	private final short[] padding = new short[4];
	private final IodineRootImpl<?> root;
	private final GuiElementType type;
	private final int internalId;
	private final Object id;
	private Object attachment;
	private GuiParentPlus<?> parent;
	private byte drawPriority;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param type the exact type of this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	protected GuiElementImpl(@NotNull IodineRootImpl<?> root, @NotNull GuiElementType type, int internalId, @NotNull Object id) {
		this.root = root;
		this.internalId = internalId;
		this.id = id;
		this.type = type;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final IodineRootImpl<?> getRoot() {
		return root;
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
	@Override
	public final T setAttachment(@Nullable Object attachment) {
		this.attachment = attachment;
		return thisT();
	}
	
	@Nullable
	@Contract(pure = true)
	@Override
	public final Object getAttachment() {
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
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final int[] getPadding() {
		return new int[]{padding[0], padding[1], padding[2], padding[3]};
	}
	
	@NotNull
	@Override
	public final T setPadding(@NotNull int[] padding) {
		Validate.isTrue(padding.length == 4, "The array containing the padding values must have a length of 4");
		for (int i = 0; i < 4; i++) {
			int value = padding[i];
			if (value != -1) {
				this.padding[i] = (short) value;
			}
		}
		return thisT();
	}
	
	
	
	@Contract(pure = true)
	@Override
	public final int getDrawPriority() {
		return drawPriority;
	}
	
	@NotNull
	@Override
	public T setDrawPriority(int priority) {
		Validate.isTrue(priority >= Byte.MIN_VALUE && priority <= Byte.MAX_VALUE,
				"The draw priority must be representable as a byte");
		drawPriority = (byte) priority;
		getRoot().flagAndUpdate(this);
		return thisT();
	}
	
	
	
	/**
	 * Serializes this element into the specified buffer.
	 * This method should only be called by {@link IodineRootImpl}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	public final void serialize(@NotNull OutputBuffer buffer) {
		buffer.putByte(type.getId());
		buffer.putInt(internalId);
		buffer.putShort(padding[0]);
		buffer.putShort(padding[1]);
		buffer.putShort(padding[2]);
		buffer.putShort(padding[3]);
		buffer.putByte(drawPriority);
		serializeImpl(buffer);
	}
	
	/**
	 * Serializes this element's type specific data into the specified buffer.
	 * This method should only be called be {@link #serialize(OutputBuffer)}.
	 *
	 * @param buffer the buffer to store the data in
	 */
	protected abstract void serializeImpl(@NotNull OutputBuffer buffer);
	
	/**
	 * Called by {@link GuiChangePacketHandler} when a viewer acts upon this element.
	 * Malicious intent should be assumed, everything should be validated.
	 * Network delays and multiple viewers should be kept in mind.
	 * It should be assumed that callbacks change the GUI,
	 * so if both a callback and {@link IodineRootImpl#flagAndUpdate(GuiElementImpl)}} are called,
	 * then {@link IodineRootImpl#flagAndAtomicUpdate(GuiElementImpl, Runnable)} should be used.
	 *
	 * @param player the sender of the packet
	 * @param buffer the packet's contents
	 */
	public abstract void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer);
	
	/**
	 * A method that is called when this element is removed from its {@link IodineRoot}.
	 * Does nothing by default, is designed to be overridden if necessary.
	 */
	public void onRemoved() {}
	
	@NotNull
	@Contract(pure = true)
	private T thisT() {
		//noinspection unchecked
		return (T) this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getInternalId() + "#" + getId();
	}
}
