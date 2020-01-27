package hu.trigary.iodine.bukkit;

import com.google.common.io.Files;
import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.gui.GuiElements;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineRoot;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * A class which exists solely for testing purposes.
 */
public class TestCommandListener implements Listener {
	private final IodineApi api = IodineApi.getInstance();
	private final IodineBukkitPlugin plugin;
	private final byte[] image;
	
	public TestCommandListener(@NotNull IodineBukkitPlugin plugin) {
		this.plugin = plugin;
		try {
			image = Files.toByteArray(new File(plugin.getDataFolder(), "image.jpg"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		
		IodineGui root = api.createGui().addElement(GuiElements.CONTAINER_LINEAR, c -> {
			c.setOrientation(true);
			IodineRoot<?> gui = c.getRoot();
			
			gui.addElement(GuiElements.BUTTON, e -> c.makeChildLast(e)
					.setText("Sample button")
					.setTooltip("Sample tooltip")
					.setPaddingBottom(5));
			
			gui.addElement(GuiElements.TEXTURE, e -> c.makeChildLast(e)
					.setTooltip("Sample tooltip"));
		});
		
		root.onClosed((gui, p) -> p.sendMessage("You closed the GUI"))
				.openFor(player);
	}
}
