package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TestCommandListener implements Listener {
	private final IodineApi api = IodineApi.getInstance();
	
	@EventHandler(ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (!event.getMessage().equals("/iodine")) {
			return;
		}
		
		event.setCancelled(true);
		api.createGui().openFor(event.getPlayer());
	}
}
