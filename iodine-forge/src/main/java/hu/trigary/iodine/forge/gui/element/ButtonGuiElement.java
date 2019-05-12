package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

public class ButtonGuiElement extends TextGuiElement {
	public ButtonGuiElement(@NotNull IodineGui gui, @NotNull GuiElementType type, int id) {
		super(gui, type, id);
	}
	
	
	
	@Override
	public Gui updateImpl() {
		return new GuiButton(getId(), 0, 0, getText());
	}
}
