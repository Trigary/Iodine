package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link IodineApi}.
 */
public class IodineApiImpl implements IodineApi {
	private final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public IodineApiImpl(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public IodinePlayer getPlayer(@NotNull Player player) {
		return plugin.getPlayer(player);
	}
	
	@NotNull
	@Override
	public IodineGui createGui() {
		return plugin.getGui().createGui();
	}
	
	@NotNull
	@Override
	public IodineOverlay createOverlay(@NotNull IodineOverlay.Anchor anchor, int horizontalOffset, int verticalOffset) {
		return plugin.getGui().createOverlay(anchor, horizontalOffset, verticalOffset);
	}
}
