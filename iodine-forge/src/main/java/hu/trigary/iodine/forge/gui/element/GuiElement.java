package hu.trigary.iodine.forge.gui.element;

import com.google.common.base.Charsets;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.forge.gui.IodineGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public abstract class GuiElement extends Gui {
	protected static final Minecraft MC = Minecraft.getMinecraft();
	private final short[] offsets = new short[4]; //TODO inherit offset -> containers set parent of children
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
	
	
	
	@NotNull
	@Contract(pure = true)
	public IodineGui getGui() {
		return gui;
	}
	
	@Contract(pure = true)
	public int getId() {
		return id;
	}
	
	
	
	public void deserialize(@NotNull ByteBuffer buffer) {
		offsets[0] = buffer.getShort();
		offsets[1] = buffer.getShort();
		offsets[2] = buffer.getShort();
		offsets[3] = buffer.getShort();
	}
	
	protected final boolean deserializeBoolean(@NotNull ByteBuffer buffer) {
		return buffer.get() == 1;
	}
	
	@NotNull
	protected final String deserializeString(@NotNull ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.getInt()];
		buffer.get(bytes);
		return new String(bytes, Charsets.UTF_8);
	}
	
	
	
	public void resolveElementReferences() { }
	
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
