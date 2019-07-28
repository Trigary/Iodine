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
				.addElement(GuiElements.TEXT, e -> e.setText("0"), 0, 0)
				.addElement(GuiElements.TEXT, e -> e.setText("300"), 300, 0)
				.addElement(GuiElements.TEXT, e -> e.setText("200"), 0, 200)
				.addElement(GuiElements.TEXT, e -> e.setText("50"), 50, 50)
				.addElement(GuiElements.TEXT, e -> e.setText("100"), 100, 100)
				.addElement(GuiElements.TEXT, e -> e.setText("150"), 150, 150)
				.addElement(GuiElements.TEXT, e -> e.setText("200"), 200, 200)
				.addElement(GuiElements.BUTTON, e -> e.setText("Click me!")
						.onClicked((ignored, p) -> p.sendMessage("You clicked me, good job!")), 50, 100)
				.addElement(GuiElements.CHECKBOX, e -> e.setChecked(true)
						.onClicked((ignored, p) -> p.sendMessage("The checkbox is checked: " + e.isChecked())), 100, 50)
				.addElement(GuiElements.BUTTON, e -> e.setText("Not editable").setEditable(false), 200, 100)
				.addElement(GuiElements.TEXT_FIELD, e -> e.setText("Your text here")
						.onChanged((ignored, oldText, newText, p) -> p.sendMessage("Old text: "
								+ oldText + ", new text: " + newText)))
				.onClosed((gui, p) -> p.sendMessage("You closed the GUI"))
				.openFor(player);
	}
}
