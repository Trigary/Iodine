package hu.trigary.iodine.bukkit.player;

import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.api.player.IodinePlayerStateChangedEvent;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.gui.IodineGuiImpl;
import hu.trigary.iodine.bukkit.gui.IodineOverlayImpl;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.bukkit.network.handler.LoginPacketHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The implementation of {@link IodinePlayer}.
 */
public class IodinePlayerImpl implements IodinePlayer {
	private final Set<IodineOverlayImpl> overlays = new HashSet<>();
	private final IodinePlugin plugin;
	private final Player player;
	private IodinePlayer.State state = IodinePlayer.State.VANILLA;
	private IodineGuiImpl openGui;
	
	/**
	 * Creates a new instance.
	 * Only a single instance should exist for a player.
	 * Should only be called by {@link PlayerManager}.
	 *
	 * @param plugin the plugin instance
	 * @param player the Bukkit player associated with this instance
	 */
	public IodinePlayerImpl(@NotNull IodinePlugin plugin, @NotNull Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	
	
	@NotNull
	@Override
	public Player getPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public IodinePlayer.State getState() {
		return state;
	}
	
	/**
	 * Sets the state of the player. While this is not validated,
	 * the rules described in the {@link IodinePlayer.State} values should be followed.
	 * Should only be called by {@link PacketListener} and {@link LoginPacketHandler}.
	 *
	 * @param state the new state of the player
	 */
	public void setState(@NotNull IodinePlayer.State state) {
		IodinePlayer.State oldState = this.state;
		this.state = state;
		Bukkit.getPluginManager().callEvent(new IodinePlayerStateChangedEvent(this, oldState, state));
	}
	
	
	
	@Override
	public IodineGuiImpl getOpenGui() {
		return openGui;
	}
	
	/**
	 * Sets or clears this player's opened GUI.
	 * Non-null values can only be replaced by null values,
	 * and null values can only be replaced by non-null values.
	 * Should only be called by {@link IodineGuiImpl}.
	 *
	 * @param gui the newly opened GUI or null, if a GUI was closed
	 */
	public void setOpenGui(@Nullable IodineGuiImpl gui) {
		Validate.isTrue(openGui == null ^ gui == null, "Opened GUIs must be closed before getting replaced, " +
				"and null values should not follow null values");
		openGui = gui;
	}
	
	@Override
	public void closeOpenGui() {
		if (openGui != null) {
			openGui.closeFor(player);
		}
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Set<IodineOverlay> getOverlays() {
		return Collections.unmodifiableSet(overlays);
	}
	
	public void addDisplayedOverlay(@NotNull IodineOverlayImpl overlay) {
		overlays.add(overlay);
	}
	
	public void removeDisplayedOverlay(@NotNull IodineOverlayImpl overlay) {
		overlays.remove(overlay);
	}
	
	@Override
	public void closeOverlay(@NotNull IodineOverlay overlay) {
		if (overlay.getViewers().contains(player)) {
			overlay.closeFor(player);
		}
	}
	
	
	
	/**
	 * Throws an exception if the state of this player is not {@link IodinePlayer.State#MODDED}.
	 */
	public void assertModded() {
		Validate.isTrue(state == IodinePlayer.State.MODDED, "The player's IodinePlayer.State must be 'MODDED'");
	}
}
