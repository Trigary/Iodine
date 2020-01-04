package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.server.IodinePlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class IodinePluginImpl extends IodinePlugin {
	private final IodineBukkitPlugin plugin;
	
	public IodinePluginImpl(@NotNull IodineBukkitPlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public IodineBukkitPlugin getBukkitPlugin() {
		return plugin;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	protected Logger getLogger() {
		return plugin.getLogger();
	}
}
