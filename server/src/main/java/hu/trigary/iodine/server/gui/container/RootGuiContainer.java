package hu.trigary.iodine.server.gui.container;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.server.gui.IodineGuiImpl;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.container.base.GuiContainerImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.ResizingByteBuffer;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RootGuiContainer extends GuiContainerImpl<RootGuiContainer> {
	private final Map<GuiElementImpl<?>, Position> children = new HashMap<>();
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI which will contain this element
	 */
	public RootGuiContainer(@NotNull IodineRootImpl<?> gui) {
		super(gui, GuiElementType.CONTAINER_ROOT, 0, new Object());
	}
	
	
	
	@NotNull
	@Override
	public Collection<GuiElement<?>> getChildren() {
		return Collections.unmodifiableCollection(children.keySet());
	}
	
	
	
	@NotNull
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		Validate.isTrue(children.put(impl, new Position((short) x, (short) y)) == null,
				"The specified element is already the child of this GUI");
		impl.setParent(this);
		getRoot().flagAndUpdate(this);
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		Validate.notNull(children.remove(child),
				"The specified element is not a child of this parent");
		getRoot().flagOnly(this);
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ResizingByteBuffer buffer) {
		buffer.putInt(children.size());
		children.forEach((element, position) -> {
			buffer.putInt(element.getInternalId());
			buffer.putShort(position.x);
			buffer.putShort(position.y);
		});
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {}
	
	
	
	private static class Position {
		final short x;
		final short y;
		
		Position(short x, short y) {
			this.x = x;
			this.y = y;
		}
	}
}