package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.server.IodinePlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of {@link IodinePlugin}.
 */
public class IodinePluginImpl extends IodinePlugin {
	private final IodineBukkitPlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineBukkitPlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public IodinePluginImpl(@NotNull IodineBukkitPlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	/**
	 * Gets the Bukkit plugin instance.
	 *
	 * @return the IodineBukkitPlugin instance
	 */
	@NotNull
	@Contract(pure = true)
	public IodineBukkitPlugin getBukkitPlugin() {
		return plugin;
	}
}
