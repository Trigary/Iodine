package hu.trigary.iodine.bukkit.api;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.bukkit.IodinePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link IodineApi}.
 */
public class IodineApiImpl extends IodineApi {
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
		return plugin.getGui().create();
	}
}
