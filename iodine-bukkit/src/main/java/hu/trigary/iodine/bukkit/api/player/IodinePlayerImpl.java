package hu.trigary.iodine.bukkit.api.player;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.api.player.PlayerState;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.api.gui.IodineGuiImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IodinePlayerImpl extends IodinePlayer {
	private final IodinePlugin plugin;
	private final Player player;
	private PlayerState state = PlayerState.VANILLA;
	private IodineGuiImpl openGui;
	
	public IodinePlayerImpl(@NotNull IodinePlugin plugin, @NotNull Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	
	
	@Override
	@NotNull
	public Player getPlayer() {
		return player;
	}
	
	@Override
	@NotNull
	public PlayerState getState() {
		return state;
	}
	
	public void setState(@NotNull PlayerState state) {
		this.state = state;
	}
	
	@Override
	public IodineGuiImpl getOpenGui() {
		return openGui;
	}
	
	
	
	@Override
	public void openGui(@NotNull IodineGui gui) {
		Validate.isTrue(player.isOnline(), "The target player must be online");
		if (openGui != null) {
			openGui.closeFor(player);
		}
		openGui = (IodineGuiImpl) gui;
		openGui.openFor(player);
	}
	
	@Override
	public void closeGui() {
		Validate.isTrue(player.isOnline(), "The target player must be online");
		if (openGui != null) {
			openGui.closeFor(player);
		}
	}
}
