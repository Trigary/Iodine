package hu.trigary.iodine.forge.gui.element.base;

import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import hu.trigary.iodine.forge.gui.container.base.GuiParent;
import hu.trigary.iodine.forge.network.out.ClientGuiChangePacket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
	
	
	
	public abstract void update();
	
	public abstract void draw(int mouseX, int mouseY, float partialTicks);
	
	
	
	public boolean onMousePressed(int mouseX, int mouseY) {
		return false;
	}
	
	public void onMouseReleased(int mouseX, int mouseY) {}
	
	public void onKeyTyped(char typedChar, int keyCode) {}
}
