package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.ImageGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link ImageGuiElement}.
 */
public class ImageGuiElementImpl extends GuiElementImpl<ImageGuiElement> implements ImageGuiElement {
	private short width = 100;
	private short height = 100;
	private String tooltip = "";
	private byte[] image;
	private ClickedAction<ImageGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ImageGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.IMAGE, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getTooltip() {
		return tooltip;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public byte[] getImage() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return image;
	}
	
	
	
	@NotNull
	@Override
	public ImageGuiElement setWidth(int width) {
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ImageGuiElement setHeight(int height) {
		this.height = (short) height;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ImageGuiElement setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ImageGuiElementImpl setImage(@NotNull byte[] image) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.image = image;
		getRoot().flagAndUpdate(this);
		throw new NotImplementedException("");
	}
	
	@NotNull
	@Override
	public ImageGuiElementImpl onClicked(@Nullable ClickedAction<ImageGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort(width);
		buffer.putShort(height);
		buffer.putString(tooltip);
		buffer.putInt(image.length);
		buffer.putBytes(image);
		throw new NotImplementedException("");
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		if (clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
