package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.ClickableElement;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ImageGuiElement extends GuiElement implements ClickableElement {
	public ImageGuiElement(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.IMAGE, id);
		throw new NotImplementedException("");
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		getGui().registerClickable(this, -1, -1);
		throw new NotImplementedException("");
	}
	
	@Override
	public Gui updateImpl() {
		throw new NotImplementedException("");
	}
	
	
	
	@Override
	public void onClicked() {
		sendChangePacket(0, buffer -> {});
	}
}
