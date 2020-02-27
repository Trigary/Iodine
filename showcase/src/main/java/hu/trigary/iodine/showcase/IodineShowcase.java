package hu.trigary.iodine.showcase;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.showcase.confirm.ConfirmShowcase;
import hu.trigary.iodine.showcase.elements.ElementsShowcase;
import hu.trigary.iodine.showcase.helloworld.HelloWorldShowcase;
import hu.trigary.iodine.showcase.minimap.MinimapShowcase;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IodineShowcase extends JavaPlugin implements Listener {
	private Map<String, hu.trigary.iodine.showcase.Showcase> showcaseCommands;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		showcaseCommands = new HashMap<>();
		showcaseCommands.put("confirm", new ConfirmShowcase(this));
		showcaseCommands.put("elements", new ElementsShowcase(this));
		showcaseCommands.put("helloworld", new HelloWorldShowcase(this));
		showcaseCommands.put("minimap", new MinimapShowcase(this));
	}
	
	
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLater(this, () -> {
			if (player.isOnline() && !IodineApi.get().isModded(player.getUniqueId())) {
				player.kickPlayer("Only players who have the Iodine mod installed are allowed!");
			}
		}, 100);
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player && IodineApi.get().isModded(((Player) sender).getUniqueId())) {
			hu.trigary.iodine.showcase.Showcase showcase = showcaseCommands.get(command.getName());
			Validate.notNull(showcase, "Command not mapped to showcase: " + command.getName());
			showcase.onCommand((Player) sender);
		} else {
			sender.sendMessage("Only modded players are allowed to use this command!");
		}
		return true;
	}
}
