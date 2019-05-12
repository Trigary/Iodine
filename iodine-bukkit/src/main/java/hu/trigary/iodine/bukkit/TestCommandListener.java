package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.element.TextGuiElement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A class which exists solely for testing purposes.
 */
public class TestCommandListener implements Listener {
	private final IodineApi api = IodineApi.getInstance();
	private final IodinePlugin plugin;
	
	public TestCommandListener(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler(ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (!event.getMessage().equals("/iodine")) {
			return;
		}
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		if (!api.isModded(player)) {
			player.sendMessage("You are not modded!");
			return;
		}
		
		api.createGui()
				.addElement(TextGuiElement.class, e -> e.setText("Hello, world!"))
				//.addElement(ButtonGuiElement.class,
				//		e -> e.setClickAction((button, p) -> p.sendMessage("You clicked the button"))
				//				.setText("Click me!"))
				.setOpenAction((gui, p) -> p.sendMessage("You opened a GUI"))
				.setCloseAction((gui, p) -> p.sendMessage("You closed a GUI"))
				.openFor(player);
	}
}
