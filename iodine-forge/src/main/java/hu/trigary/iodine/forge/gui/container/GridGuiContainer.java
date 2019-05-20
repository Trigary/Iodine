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
	private int[] children;
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
		children = new int[columnCount * rowCount];
		Arrays.setAll(children, i ->  buffer.getInt());
	}
	
	@Override
	public Gui updateImpl() {
	
	}
}
