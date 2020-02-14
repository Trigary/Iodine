package hu.trigary.iodine.server.gui.container;

import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.server.gui.IodineGuiImpl;
import hu.trigary.iodine.server.gui.IodineRootImpl;
import hu.trigary.iodine.server.gui.container.base.GuiContainerImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link GuiContainerImpl} that is the backing container of {@link IodineRootImpl} instance.
 * Exactly one instance of this class must exist at the internal ID of 0 in each {@link IodineRootImpl}.
 */
public class RootGuiContainer extends GuiContainerImpl<RootGuiContainer> {
	private final Map<GuiElementImpl<?>, Position> children = new HashMap<>();
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineGuiImpl}.
	 *
	 * @param root the instance which will contain this element
	 */
	public RootGuiContainer(@NotNull IodineRootImpl<?> root) {
		super(root, GuiElementType.CONTAINER_ROOT, 0, new Object());
	}
	
	
	
	@NotNull
	@Override
	public Collection<GuiElement<?>> getChildren() {
		return Collections.unmodifiableCollection(children.keySet());
	}
	
	
	
	@NotNull
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		if (impl.getParent() == this) {
			Position position = children.get(impl);
			if (position.x == x && position.y == y) {
				return element;
			}
		}
		
		impl.setParent(this);
		children.put(impl, new Position((short) x, (short) y));
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
	public void serializeImpl(@NotNull OutputBuffer buffer) {
		buffer.putInt(children.size());
		children.forEach((element, position) -> {
			buffer.putInt(element.getInternalId());
			buffer.putShort(position.x);
			buffer.putShort(position.y);
		});
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull IodinePlayerBase player, @NotNull InputBuffer buffer) {}
	
	
	
	private static class Position {
		final short x;
		final short y;
		
		Position(short x, short y) {
			this.x = x;
			this.y = y;
		}
	}
}
