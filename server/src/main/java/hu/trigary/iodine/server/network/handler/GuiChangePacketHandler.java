package hu.trigary.iodine.server.network.handler;

import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.IodineGuiImpl;
import hu.trigary.iodine.server.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.server.network.PacketListener;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.logging.Level;

/**
 * The handler of {@link PacketType#CLIENT_GUI_CHANGE}.
 */
public class GuiChangePacketHandler extends PacketHandler {
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link PacketListener}.
	 *
	 * @param plugin the plugin instance
	 */
	public GuiChangePacketHandler(@NotNull IodinePlugin plugin) {
		super(plugin);
	}
	
	
	
	@Override
	public void handle(@NotNull IodinePlayerBase player, @NotNull ByteBuffer message) {
		IodineGuiImpl gui = player.getOpenGui();
		if (gui == null || gui.getId() != message.getInt()) {
			getPlugin().log(Level.OFF, "Network > updating GUI failed: no open GUI by id");
			return;
		}
		
		getPlugin().log(Level.OFF, "Network > updating GUI {0}", gui.getId());
		GuiElementImpl<?> element = gui.getElement(message.getInt());
		if (element == null) {
			getPlugin().log(Level.OFF, "Network > updating element failed: no element by id");
		} else {
			getPlugin().log(Level.OFF, "Network > updating element {0}", element.getInternalId());
			element.handleChangePacket(player, message);
		}
	}
}
