package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
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
	public void handle(@NotNull IodinePlayerImpl player, @NotNull ByteBuffer message) {
		IodineGuiImpl gui = player.getOpenGui();
		if (gui == null || gui.getId() != message.getInt()) {
			plugin.log(Level.OFF, "Network > updating GUI failed: no open GUI by id");
			return;
		}
		
		plugin.log(Level.OFF, "Network > updating GUI {0}", gui.getId());
		GuiElementImpl<?> element = gui.getElement(message.getInt());
		if (element == null) {
			plugin.log(Level.OFF, "Network > updating element failed: no element by id");
		} else {
			plugin.log(Level.OFF, "Network > updating element {0}", element.getId());
			element.handleChangePacket(player.getPlayer(), message);
		}
	}
}
