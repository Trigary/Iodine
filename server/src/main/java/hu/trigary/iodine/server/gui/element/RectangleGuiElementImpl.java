package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.IodineColor;
import hu.trigary.iodine.api.gui.element.RectangleGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link RectangleGuiElement}.
 */
public class RectangleGuiElementImpl extends GuiElementImpl<RectangleGuiElement> implements RectangleGuiElement {
	private short width = 10;
	private short height = 10;
	private String tooltip = "";
	private IodineColor color = IodineColor.WHITE;
	private ClickedAction<RectangleGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public RectangleGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.RECTANGLE, internalId, id);
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
	@Override
	public IodineColor getColor() {
		return color;
	}
	
	
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setWidth(int width) {
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setHeight(int height) {
		this.height = (short) height;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public RectangleGuiElementImpl setColor(@NotNull IodineColor color) {
		this.color = color;
		getRoot().flagAndUpdate(this);
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
		buffer.putString(tooltip);
		buffer.putByte((byte) color.getRed());
		buffer.putByte((byte) color.getGreen());
		buffer.putByte((byte) color.getBlue());
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		if (clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
