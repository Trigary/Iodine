package hu.trigary.iodine.forge.gui.element.base;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.container.base.GuiParent;
import hu.trigary.iodine.forge.network.out.ClientGuiChangePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public abstract class GuiElement {
	protected static final Minecraft MC = Minecraft.getMinecraft();
	private final IodineGui gui;
	private final GuiElementType type;
	private final int id;
	private GuiParent parent;
	private int xOffset;
	private int yOffset;
	private Gui minecraftGuiObject;
	private boolean isButton;
	
	protected GuiElement(@NotNull IodineGui gui, @NotNull GuiElementType type, int id) {
		this.gui = gui;
		this.type = type;
		this.id = id;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public final IodineGui getGui() {
		return gui;
	}
	
	@Contract(pure = true)
	public final int getId() {
		return id;
	}
	
	
	
	public final int getX() {
		return parent.getLeft() + xOffset;
	}
	
	public final int getY() {
		return parent.getTop() + yOffset;
	}
	
	
	
	public void deserialize(@NotNull ByteBuffer buffer) { }
	
	protected final void sendChangePacket(int dataLength, @NotNull Consumer<ByteBuffer> dataProvider) {
		byte[] data = new byte[dataLength];
		dataProvider.accept(ByteBuffer.wrap(data));
		getGui().getMod().getNetwork().send(new ClientGuiChangePacket(getGui().getId(), id, data));
	}
	
	
	
	public void initialize(@NotNull GuiParent parent, int xOffset, int yOffset) {
		this.parent = parent;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	
	
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
