package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.gui.element.ProgressBarGuiElement;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * A class which exists solely for testing purposes.
 */
public class TestCommandListener implements Listener {
	private final IodineApi api = IodineApi.getInstance();
	private final IodineBukkitPlugin plugin;
	
	public TestCommandListener(@NotNull IodineBukkitPlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler(ignoreCancelled = true)
	public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
		if (!event.getMessage().equals("/iodine")) {
			return;
		}
		
		event.setCancelled(true);
		IodinePlayer player = api.getPlayer(event.getPlayer().getUniqueId());
		if (!player.isModded()) {
			player.sendMessage("You are not modded!");
			return;
		}
		
		Object id = new Object();
		IodineGui root = api.createGui().addElement(GuiElements.CONTAINER_LINEAR, c -> {
			c.setOrientation(true);
			IodineRoot<?> gui = c.getRoot();
			
			gui.addElement(GuiElements.BUTTON, e -> c.makeChildLast(e)
					.setText("Sample button")
					.setPaddingBottom(5));
			
			gui.addElement(id, GuiElements.PROGRESS_BAR, e -> c.makeChildLast(e)
					.setTooltip("Some tooltip")
					.setWidth(100)
					.setOrientation(true));
		});
		
		BukkitRunnable task = new BukkitRunnable() {
			@Override
			public void run() {
				ProgressBarGuiElement element = (ProgressBarGuiElement) root.getElement(id);
				float newProgress = element.getProgress() + 0.01f;
				element.setProgress(newProgress > 1f ? 0 : newProgress);
			}
		};
		task.runTaskTimer(plugin, 1, 1);
		
		root.onClosed((gui, p) -> {
			p.sendMessage("You closed the GUI");
			task.cancel();
		}).openFor(player);
	}
}
