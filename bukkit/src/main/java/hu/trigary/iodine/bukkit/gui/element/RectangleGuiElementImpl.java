package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.IodineColor;
import hu.trigary.iodine.api.gui.element.RectangleGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link RectangleGuiElement}.
 */
public class RectangleGuiElementImpl extends GuiElementImpl<RectangleGuiElement> implements RectangleGuiElement {
	private short width = 10;
	private short height = 10;
	private IodineColor color;
	private ClickedAction<RectangleGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public RectangleGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.RECTANGLE, internalId, id);
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
	@Override
	public IodineColor getColor() {
		return color;
	}
	
	
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setWidth(int width) {
		this.width = (short) width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setHeight(int height) {
		this.height = (short) height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setColor(@NotNull IodineColor color) {
		this.color = color;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl onClicked(@Nullable ClickedAction<RectangleGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort(width);
		buffer.putShort(height);
		buffer.putByte((byte) color.getRed());
		buffer.putByte((byte) color.getGreen());
		buffer.putByte((byte) color.getBlue());
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {
		if (clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
