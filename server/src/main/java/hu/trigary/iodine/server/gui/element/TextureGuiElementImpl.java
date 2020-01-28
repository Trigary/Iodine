package hu.trigary.iodine.server.gui.element;

import hu.trigary.iodine.api.gui.element.TextureGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * The implementation of {@link TextureGuiElement}.
 */
public class TextureGuiElementImpl extends GuiElementImpl<TextureGuiElement> implements TextureGuiElement {
	private static final Pattern TEXTURE_PATTERN = Pattern.compile("[a-z0-9_.-]+:[a-z0-9/_.-]+");
	private final short[] textureData = new short[6];
	private short width = 100;
	private short height = 100;
	private String tooltip = "";
	private String texture = "minecraft:textures/block/dirt.png";
	private ResizeMode resizeMode = ResizeMode.STRETCH;
	private ClickedAction<TextureGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public TextureGuiElementImpl(@NotNull IodineRootImpl<?> root, int internalId, @NotNull Object id) {
		super(root, GuiElementType.TEXTURE, internalId, id);
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
	public String getTexture() {
		return texture;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public int[] getTextureData() {
		int[] array = new int[textureData.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = textureData[i];
		}
		return array;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public ResizeMode getResizeMode() {
		return resizeMode;
	}
	
	
	
	@NotNull
	@Override
	public TextureGuiElementImpl setWidth(int width) {
		this.width = (short) width;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextureGuiElementImpl setHeight(int height) {
		this.height = (short) height;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextureGuiElementImpl setTooltip(@NotNull String tooltip) {
		this.tooltip = tooltip;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextureGuiElementImpl setTexture(@NotNull String namespacedKey, int textureFileWidth, int textureFileHeight,
			int textureOffsetX, int textureOffsetY, int textureWidth, int textureHeight) {
		Validate.isTrue(TEXTURE_PATTERN.matcher(namespacedKey).matches(),
				"The texture resource location must be a valid namespaced key");
		Validate.isTrue(textureFileWidth >= textureOffsetX + textureWidth
						&& textureFileHeight >= textureOffsetY + textureHeight,
				"The texture must fit inside its containing file");
		texture = namespacedKey;
		textureData[0] = (short) textureFileWidth;
		textureData[1] = (short) textureFileHeight;
		textureData[2] = (short) textureOffsetX;
		textureData[3] = (short) textureOffsetY;
		textureData[4] = (short) textureWidth;
		textureData[5] = (short) textureHeight;
		return this;
	}
	
	@NotNull
	@Override
	public TextureGuiElementImpl setResizeMode(@NotNull ResizeMode resizeMode) {
		this.resizeMode = resizeMode;
		getRoot().flagAndUpdate(this);
		return this;
	}
	
	@NotNull
	@Override
	public TextureGuiElementImpl onClicked(@Nullable ClickedAction<TextureGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putShort(width);
		buffer.putShort(height);
		buffer.putString(tooltip);
		buffer.putString(texture);
		buffer.putShort(textureData[0] <= 0 ? width : textureData[0]);
		buffer.putShort(textureData[1] <= 0 ? height : textureData[1]);
		buffer.putShort(textureData[2]);
		buffer.putShort(textureData[3]);
		buffer.putShort(textureData[4] <= 0 ? width : textureData[4]);
		buffer.putShort(textureData[5] <= 0 ? height : textureData[5]);
		buffer.putByte((byte) resizeMode.ordinal());
	}
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {
		if (clickedAction != null) {
			clickedAction.accept(this, player);
		}
	}
}
