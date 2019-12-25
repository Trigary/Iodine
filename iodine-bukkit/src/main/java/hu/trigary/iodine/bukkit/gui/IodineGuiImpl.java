package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link IodineGui}.
 */
public class IodineGuiImpl extends GuiBaseImpl<IodineGui> implements IodineGui {
	private ClosedAction closedAction;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiBaseManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 */
	public IodineGuiImpl(@NotNull IodinePlugin plugin, int id) {
		super(plugin, id);
	}
	
	
	
	@NotNull
	@Override
	public IodineGuiImpl onClosed(@Nullable ClosedAction action) {
		closedAction = action;
		return this;
	}
	
	
	
	@Override
	protected void onPreOpened(@NotNull IodinePlayerImpl iodinePlayer) {
		IodineGuiImpl previous = iodinePlayer.getOpenGui();
		if (previous != null) {
			previous.closeForNoPacket(iodinePlayer, false);
		}
	}
	
	@Override
	protected void onOpened(@NotNull IodinePlayerImpl iodinePlayer) {
		iodinePlayer.setOpenGui(this);
	}
	
	@Override
	protected void sendClosePacket(@NotNull NetworkManager network, @NotNull Player player) {
		network.send(player, PacketType.SERVER_GUI_CLOSE);
	}
	
	@Override
	protected void onClosed(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer) {
		iodinePlayer.setOpenGui(null);
		if (byPlayer && closedAction != null) {
			closedAction.accept(this, iodinePlayer.getPlayer());
		}
	}
	
	
	
	@Override
	protected void serializeOpenStart(@NotNull ResizingByteBuffer buffer) {
		buffer.putByte(PacketType.SERVER_GUI_OPEN.getId());
		buffer.putInt(getId());
	}
	
	@Override
	protected void serializeUpdateStart(@NotNull ResizingByteBuffer buffer) {
		buffer.putByte(PacketType.SERVER_GUI_CHANGE.getId());
	}
}
