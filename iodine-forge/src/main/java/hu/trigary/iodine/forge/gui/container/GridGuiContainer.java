package hu.trigary.iodine.forge.gui.container;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.element.GuiElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class GridGuiContainer extends GuiElement {
	private int[] childrenTemp;
	private GuiElement[] children;
	private int columnCount;
	private int rowCount;
	
	public GridGuiContainer(@NotNull IodineGui gui, int id) {
		super(gui, GuiElementType.CONTAINER_GRID, id);
	}
	
	
	
	@Override
	public void deserialize(@NotNull ByteBuffer buffer) {
		super.deserialize(buffer);
		columnCount = buffer.getInt();
		rowCount = buffer.getInt();
		childrenTemp = new int[columnCount * rowCount];
		Arrays.setAll(children, i ->  buffer.getInt());
	}
	
	@Override
	public void resolveElementReferences() {
		IodineGui gui = getGui();
		children = new GuiElement[childrenTemp.length];
		Arrays.setAll(children, i -> gui.getElement(childrenTemp[i]));
		childrenTemp = null;
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
