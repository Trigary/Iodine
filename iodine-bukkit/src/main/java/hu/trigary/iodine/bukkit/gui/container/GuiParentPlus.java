package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.base.GuiParent;
import hu.trigary.iodine.bukkit.gui.element.GuiElementImpl;
import org.jetbrains.annotations.NotNull;

public interface GuiParentPlus<T extends GuiParent<T>> extends GuiParent<T> {
	void removeChild(@NotNull GuiElementImpl<?> child);
}
