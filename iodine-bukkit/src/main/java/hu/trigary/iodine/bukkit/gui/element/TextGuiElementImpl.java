package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.TextGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link TextGuiElement}.
 */
public class TextGuiElementImpl extends GuiElementImpl<TextGuiElement> implements TextGuiElement {
	private int width = 200;
	private int height = 20;
	private String text = "";
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextGuiElementImpl(@NotNull GuiBaseImpl<?> gui, short internalId, @NotNull Object id) {
		super(gui, GuiElementType.TEXT, internalId, id);
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
	public String getText() {
		return text;
	}
	
	
	
	@NotNull
	@Override
	public TextGuiElementImpl setWidth(int width) {
		Validate.isTrue(width > 0 && width <= Short.MAX_VALUE, "The width must be positive and at most Short.MAX_VALUE");
		this.width = width;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextGuiElementImpl setHeight(int height) {
		Validate.isTrue(height > 0 && height <= Short.MAX_VALUE, "The height must be positive and at most Short.MAX_VALUE");
		this.height = height;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextGuiElementImpl setText(@NotNull String text) {
		this.text = text;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort((short) width);
		buffer.putShort((short) height);
		buffer.putString(text);
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {}
}
