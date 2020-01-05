package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.client.gui.IodineRoot;
import hu.trigary.iodine.client.gui.element.ImageGuiElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link ImageGuiElement}.
 */
public class ImageGuiElementImpl extends ImageGuiElement {
	/**
	 * Creates a new instance.
	 *
	 * @param root the instance which will contain this element
	 * @param id the internal ID of this element
	 */
	public ImageGuiElementImpl(@NotNull IodineRoot root, int id) {
		super(root, id);
	}



	@Override
	protected void updateImpl(int width, int height, int positionX, int positionY) {
		throw new NotImplementedException("");
	}

	@Override
	protected void drawImpl(int width, int height, int positionX, int positionY, int mouseX, int mouseY, float partialTicks) {

	}
}
