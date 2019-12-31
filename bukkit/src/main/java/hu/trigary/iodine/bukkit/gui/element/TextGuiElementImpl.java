package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.TextGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link TextGuiElement}.
 */
public class TextGuiElementImpl extends GuiElementImpl<TextGuiElement> implements TextGuiElement {
	private short width = 200;
	private String text = "";
	private Alignment alignment = Alignment.CENTER;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextGuiElementImpl(@NotNull GuiBaseImpl<?> gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.TEXT, internalId, id);
	}
	
	
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public String getText() {
		return text;
	}
	
	@NotNull
	@Override
	public Alignment getAlignment() {
		return alignment;
	}
	
	
	
	@NotNull
	@Override
	public TextGuiElementImpl setWidth(int width) {
		this.width = (short) width;
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
	
	@NotNull
	@Override
	public TextGuiElementImpl setAlignment(@NotNull Alignment alignment) {
		this.alignment = alignment;
		getGui().flagAndUpdate(this);
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putShort(width);
		buffer.putString(text);
		buffer.putByte((byte) alignment.getNumber());
	}
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {}
}
