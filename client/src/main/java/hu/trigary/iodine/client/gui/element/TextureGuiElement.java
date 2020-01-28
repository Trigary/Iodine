package hu.trigary.iodine.client.gui.element;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.client.IntPair;
import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.base.GuiElement;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The implementation of {@link hu.trigary.iodine.backend.GuiElementType#TEXTURE}.
 */
public abstract class TextureGuiElement extends GuiElement {
	protected int width;
	protected int height;
	protected String tooltip;
	protected String texture;
	protected int fileWidth;
	protected int fileHeight;
	protected int offsetX;
	protected int offsetY;
	protected int textureWidth;
	protected int textureHeight;
	protected int resizeMode;
	
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	protected TextureGuiElement(@NotNull IodineRoot root, int id) {
		super(root, id);
	}
	
	
	
	@Override
	protected final void deserializeImpl(@NotNull InputBuffer buffer) {
		width = buffer.readShort();
		height = buffer.readShort();
		tooltip = buffer.readString();
		texture = buffer.readString();
		fileWidth = buffer.readShort();
		fileHeight = buffer.readShort();
		offsetX = buffer.readShort();
		offsetY = buffer.readShort();
		textureWidth = buffer.readShort();
		textureHeight = buffer.readShort();
		resizeMode = buffer.readByte();
	}
	
	@NotNull
	@Override
	protected final IntPair calculateSizeImpl(int screenWidth, int screenHeight) {
		return new IntPair(width, height);
	}
	
	/**
	 * Should be called when the user clicked this element.
	 * Calls {@link #sendChangePacket(Consumer)} internally after doing sanity checks.
	 */
	protected final void onChanged() {
		sendChangePacket(b -> {});
	}
}
