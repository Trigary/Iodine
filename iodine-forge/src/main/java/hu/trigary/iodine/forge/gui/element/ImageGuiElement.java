package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ImageGuiElement extends GuiElement {
	public ImageGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.IMAGE, id);
		throw new UnsupportedOperationException();
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Gui updateImpl() {
		throw new UnsupportedOperationException();
	}
}
