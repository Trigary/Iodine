package hu.trigary.iodine.bukkit.gui.container;

import hu.trigary.iodine.api.gui.container.base.GuiContainer;
import hu.trigary.iodine.api.gui.element.base.GuiElement;
import hu.trigary.iodine.backend.GuiElementType;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.container.base.GuiParentPlus;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RootGuiContainer extends GuiElementImpl<RootGuiContainer>
		implements GuiContainer<RootGuiContainer>, GuiParentPlus<RootGuiContainer> {
	private final Map<GuiElementImpl<?>, Position> children = new HashMap<>();
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineGuiImpl}.
	 *
	 * @param gui the GUI which will contain this element
	 */
	public RootGuiContainer(@NotNull IodineGuiImpl gui) {
		super(gui, GuiElementType.CONTAINER_ROOT, 0, new Object());
	}
	
	
	
	@NotNull
	@Override
	public Collection<GuiElement<?>> getChildren() {
		return Collections.unmodifiableCollection(children.keySet());
	}
	
	
	
	
	@NotNull
	public <E extends GuiElement<E>> E makeChild(@NotNull E element, int x, int y) {
		Validate.isTrue(x >= 0 && y >= 0 && x <= Short.MAX_VALUE && y <= Short.MAX_VALUE,
				"The element's render position must be at least 0 and at most Short.MAX_VALUE");
		//TODO actually allow negative values, as long as it can't crash the client
		GuiElementImpl<?> impl = (GuiElementImpl<?>) element;
		Validate.isTrue(children.put(impl, new Position(x, y)) == null,
				"The specified element is already the child of this GUI");
		impl.setParent(this);
		getGui().flagAndUpdate(this);
		return element;
	}
	
	@Override
	public void removeChild(@NotNull GuiElementImpl<?> child) {
		Validate.notNull(children.remove(child),
				"The specified element is not a child of this parent");
		getGui().flagOnly(this);
	}
	
	
	
	@Override
	public void serializeImpl(@NotNull ByteBuffer buffer) {
		buffer.putInt(children.size());
		children.forEach((element, position) -> {
			buffer.putInt(element.getInternalId());
			buffer.putShort((short) position.x);
			buffer.putShort((short) position.y);
		});
	}
	
	
	
	@Override
	public void handleChangePacket(@NotNull Player player, @NotNull ByteBuffer message) {}
	
	
	
	private static class Position {
		final int x;
		final int y;
		
		Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
