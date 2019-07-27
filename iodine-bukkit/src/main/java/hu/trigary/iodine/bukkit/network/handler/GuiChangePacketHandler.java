package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.element.base.GuiElementImpl;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.backend.PacketType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

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
	public void handle(@NotNull Player player, @NotNull ByteBuffer message) {
		IodineGuiImpl gui = plugin.getGui().getGui(message.getInt());
		if (gui != null && gui.getViewers().contains(player)) {
			GuiElementImpl<?> element = gui.getElement(message.getInt());
			if (element != null) {
				element.handleChangePacket(player, message);
			}
		}
	}
}
