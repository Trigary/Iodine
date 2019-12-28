package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ImageGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.IodineUtils;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
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
	private byte[] image;
	private ClickedAction<ImageGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ImageGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.IMAGE, internalId, id);
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
	public byte[] getImage() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return image;
	}
	
	
	
	@NotNull
	@Override
	public ImageGuiElement setWidth(int width) {
		IodineUtils.validateWidth(width);
		this.width = (short) width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ImageGuiElement setHeight(int height) {
		IodineUtils.validateHeight(height);
		this.height = (short) height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public ImageGuiElementImpl setImage(@NotNull byte[] image) {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.image = image;
		getGui().flagAndUpdate(this);
		throw new NotImplementedException();
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
		buffer.putInt(image.length);
		buffer.putBytes(image);
		throw new NotImplementedException();
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
