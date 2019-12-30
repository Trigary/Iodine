package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.gui.element.ButtonGuiElement;
import hu.trigary.iodine.api.gui.element.TextFieldGuiElement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

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
		
		Object id = new Object();
		
		Consumer<ButtonGuiElement> initializer = e -> e
				.setText("Click me!")
				.onClicked((ignored, p) -> {
					p.sendMessage("Button clicked");
					e.getGui().atomicUpdate(gui -> {
						gui.removeElement(e);
						TextFieldGuiElement text = (TextFieldGuiElement) gui.getElement(id);
						text.setText(text.getText() + "x");
					});
				});
		
		api.createOverlay(IodineOverlay.Anchor.TOP_RIGHT, 0, 0)
				.addElement(GuiElements.BUTTON, initializer, 50, 50)
				.addElement(GuiElements.BUTTON, initializer, 100, 50)
				.addElement(GuiElements.BUTTON, initializer, 50, 100)
				.addElement(GuiElements.BUTTON, initializer, 100, 100)
				.addElement(id, GuiElements.TEXT_FIELD, e -> e.setText("Your text here")
						.setRegex("[a-zA-Z ]*")
						.onChanged((ignored, oldText, newText, p) -> p.sendMessage("Old text: "
								+ oldText + ", new text: " + newText)))
				//.onClosed((gui, p) -> p.sendMessage("You closed the GUI"))
				.openFor(player);
	}
}
