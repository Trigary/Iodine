package hu.trigary.iodine.bukkit.gui;

import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.IodineUtils;
import hu.trigary.iodine.bukkit.gui.container.base.GuiBaseImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import hu.trigary.iodine.bukkit.network.ResizingByteBuffer;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class IodineOverlayImpl extends GuiBaseImpl<IodineOverlay> implements IodineOverlay {
	private final Anchor anchor;
	private final short horizontalOffset;
	private final short verticalOffset;
	private byte drawPriority;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiBaseManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 * @param anchor the specified anchor
	 * @param horizontalOffset the overlay's horizontal offset
	 * @param verticalOffset the overlay's vertical offset
	 */
	public IodineOverlayImpl(@NotNull IodinePlugin plugin, int id,
			@NotNull Anchor anchor, int horizontalOffset, int verticalOffset) {
		super(plugin, id);
		this.anchor = anchor;
		IodineUtils.validateRange(OFFSET_LOWER_BOUND, OFFSET_UPPER_BOUND, horizontalOffset, "offset");
		IodineUtils.validateRange(OFFSET_LOWER_BOUND, OFFSET_UPPER_BOUND, verticalOffset, "offset");
		this.horizontalOffset = (short) horizontalOffset;
		this.verticalOffset = (short) verticalOffset;
	}
	
	
	
	@NotNull
	@Override
	public Anchor getAnchor() {
		return anchor;
	}
	
	@Override
	public int getHorizontalOffset() {
		return horizontalOffset;
	}
	
	@Override
	public int getVerticalOffset() {
		return verticalOffset;
	}
	
	@Override
	@Contract(pure = true)
	public int getDrawPriority() {
		return drawPriority;
	}
	
	@Override
	public void setDrawPriority(int priority) {
		IodineUtils.validateRange(PRIORITY_LOWER_BOUND, PRIORITY_UPPER_BOUND, priority, "draw priority");
		drawPriority = (byte) priority;
		executeUpdate();
	}
	
	
	
	@Override
	protected void onPreOpened(@NotNull IodinePlayerImpl iodinePlayer) {}
	
	@Override
	protected void onOpened(@NotNull IodinePlayerImpl iodinePlayer) {
		iodinePlayer.addDisplayedOverlay(this);
	}
	
	@Override
	protected void sendClosePacket(@NotNull NetworkManager network, @NotNull Player player) {
		network.send(player, PacketType.SERVER_OVERLAY_CLOSE, 4, b -> b.putInt(getId()));
	}
	
	@Override
	protected void onClosed(@NotNull IodinePlayerImpl iodinePlayer, boolean byPlayer) {
		iodinePlayer.removeDisplayedOverlay(this);
	}
	
	
	
	@Override
	protected void serializeOpenStart(@NotNull ResizingByteBuffer buffer) {
		buffer.putByte(PacketType.SERVER_OVERLAY_OPEN.getId());
		buffer.putInt(getId());
		buffer.putByte((byte) anchor.getNumber());
		buffer.putByte(drawPriority);
	}
	
	@Override
	protected void serializeUpdateStart(@NotNull ResizingByteBuffer buffer) {
		buffer.putByte(PacketType.SERVER_OVERLAY_CHANGE.getId());
		buffer.putInt(getId());
		buffer.putByte(drawPriority);
	}
}
