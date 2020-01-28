package hu.trigary.iodine.server.gui;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.network.NetworkManager;
import hu.trigary.iodine.server.player.IodinePlayerBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of {@link IodineGui}.
 */
public class IodineGuiImpl extends IodineRootImpl<IodineGui> implements IodineGui {
	private ClosedAction closedAction;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link IodineRootManager}.
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
	protected void onPreOpened(@NotNull IodinePlayerBase player) {
		IodineGuiImpl previous = player.getOpenGui();
		if (previous != null) {
			previous.closeForNoPacket(player, false);
		}
	}
	
	@Override
	protected void onOpened(@NotNull IodinePlayerBase player) {
		player.setOpenGui(this);
	}
	
	@Override
	protected void sendClosePacket(@NotNull NetworkManager network, @NotNull IodinePlayerBase player) {
		network.send(player, PacketType.SERVER_GUI_CLOSE);
	}
	
	@Override
	protected void onClosed(@NotNull IodinePlayerBase iodinePlayer, boolean byPlayer) {
		iodinePlayer.setOpenGui(null);
		if (byPlayer && closedAction != null) {
			closedAction.accept(this, iodinePlayer);
		}
	}
	
	
	
	@Override
	protected void serializeOpenStart(@NotNull OutputBuffer buffer) {
		buffer.putByte(PacketType.SERVER_GUI_OPEN.getId());
		buffer.putInt(getId());
	}
	
	@Override
	protected void serializeUpdateStart(@NotNull OutputBuffer buffer) {
		buffer.putByte(PacketType.SERVER_GUI_CHANGE.getId());
	}
}
