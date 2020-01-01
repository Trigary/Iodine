package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.container.base.GuiBase;
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
	public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
		if (!event.getMessage().equals("/iodine")) {
			return;
		}
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		if (!api.isModded(player)) {
			player.sendMessage("You are not modded!");
			return;
		}
		
		//TODO test draw priorities, padding, containers, widths, heights
		
		api.createGui().addElement(GuiElements.CONTAINER_LINEAR, c -> {
			c.setOrientation(true);
			GuiBase<?> gui = c.getGui();
			
			gui.addElement(GuiElements.CHECKBOX, e -> c.makeChildLast(e)
					.onClicked((ee, p) -> p.sendMessage("Checked: " + e.isChecked())));
			
		}).onClosed((gui, p) -> p.sendMessage("You closed the GUI"))
				.openFor(player);
	}
}
