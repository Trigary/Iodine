package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
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
				.addElement(GuiElements.CONTAINER_LINEAR, container -> container.getGui()
						.addElement(GuiElements.TEXT, e -> container.makeChildLast(e)
								.setText("Hello, world!"))
						.addElement(GuiElements.BUTTON, e -> container.makeChildLast(e)
								.setText("Click me!")
								.onClicked((b, p) -> p.sendMessage("You clicked the button"))))
				.onClosed((gui, p) -> p.sendMessage("You closed a GUI"))
				.openFor(player);
	}
}
