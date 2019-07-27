package hu.trigary.iodine.forge.gui.container.base;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.base.GuiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class GuiContainer extends GuiElement implements GuiParent {
	protected GuiContainer(@NotNull IodineGui gui, @NotNull GuiElementType type, int id) {
		super(gui, type, id);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	protected final GuiElement[] resolveChildren(@NotNull int[] childrenTemp) {
		IodineGui gui = getGui();
		GuiElement[] children = new GuiElement[childrenTemp.length];
		Arrays.setAll(children, i -> gui.getElement(childrenTemp[i]));
		return children;
	}
}
