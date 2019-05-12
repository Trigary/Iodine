package hu.trigary.iodine.forge.gui.element;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public abstract class GuiElement extends Gui {
	protected static final Minecraft MC = Minecraft.getMinecraft();
	private final IodineGui gui;
	private final GuiElementType type;
	private final int id;
	private Gui minecraftGuiObject;
	private boolean isButton;
	
	protected GuiElement(@NotNull IodineGui gui, @NotNull GuiElementType type, int id) {
		this.gui = gui;
		this.type = type;
		this.id = id;
	}
	
	
	
	public int getId() {
		return id;
	}
	
	
	
	public void deserialize(@NotNull ByteBuffer buffer) { }
	
	public final void update() {
		minecraftGuiObject = updateImpl();
		isButton = minecraftGuiObject instanceof GuiButton;
	}
	
	@Nullable
	protected abstract Gui updateImpl();
	
	
	
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if (isButton) {
			((GuiButton) minecraftGuiObject).drawButton(MC, mouseX, mouseY, partialTicks);
		} else {
			((GuiLabel) minecraftGuiObject).drawLabel(MC, mouseX, mouseY);
		}
	}
}
