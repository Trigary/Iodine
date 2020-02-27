package hu.trigary.iodine.showcase;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Showcase {
	protected final IodineShowcase plugin;
	
	protected Showcase(@NotNull IodineShowcase plugin) {
		this.plugin = plugin;
	}
	
	
	
	public abstract void onCommand(@NotNull Player player);
}
