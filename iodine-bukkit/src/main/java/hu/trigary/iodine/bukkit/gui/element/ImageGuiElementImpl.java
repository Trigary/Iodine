package hu.trigary.iodine.bukkit.gui.element;

import hu.trigary.iodine.api.gui.element.ImageGuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The implementation of {@link ImageGuiElement}.
 */
public class ImageGuiElementImpl extends GuiElementImpl<ImageGuiElement> implements ImageGuiElement {
	private ClickedAction<ImageGuiElement> clickedAction;
	
	/**
	 * Creates a new instance.
	 *
	 * @param gui the GUI which will contain this element
	 * @param internalId the internal ID of this element
	 * @param id the API-friendly ID of this element
	 */
	public ImageGuiElementImpl(@NotNull IodineGuiImpl gui, int internalId, @NotNull Object id) {
		super(gui, GuiElementType.IMAGE, internalId, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public byte[] getImage() {
		throw new UnsupportedOperationException();
	}
	
	
	
	@NotNull
	@Override
	public ImageGuiElementImpl setImage(@NotNull byte[] image) {
		throw new UnsupportedOperationException();
	}
	
	@NotNull
	@Override
	public ImageGuiElementImpl onClicked(@Nullable ClickedAction<ImageGuiElement> action) {
		clickedAction = action;
		return this;
	}
	
	
	
	@Override
	public void serialize(@NotNull ByteBuffer buffer) {
		super.serialize(buffer);
		throw new UnsupportedOperationException();
	}
}
