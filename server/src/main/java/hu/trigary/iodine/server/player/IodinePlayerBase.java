package hu.trigary.iodine.server.player;

import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.api.player.IodinePlayerStateChangedEvent;
import hu.trigary.iodine.server.IodinePlugin;
import hu.trigary.iodine.server.gui.IodineGuiImpl;
import hu.trigary.iodine.server.gui.IodineOverlayImpl;
import hu.trigary.iodine.server.network.PacketListener;
import hu.trigary.iodine.server.network.handler.LoginPacketHandler;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The implementation of {@link IodinePlayer}, a base class for an actual implementation.
 */
public abstract class IodinePlayerBase implements IodinePlayer {
	private final Set<IodineOverlayImpl> overlays = new HashSet<>();
	private final IodinePlugin plugin;
	private IodinePlayer.State state = IodinePlayer.State.VANILLA;
	private IodineGuiImpl openGui;
	
	/**
	 * Creates a new instance.
	 * Only a single instance should exist for a player.
	 * Should only be called by {@link PlayerManager}.
	 *
	 * @param plugin the plugin instance
	 */
	protected IodinePlayerBase(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final IodinePlayer.State getState() {
		return state;
	}
	
	/**
	 * Sets the state of the player. While this is not validated,
	 * the rules described in the {@link IodinePlayer.State} values should be followed.
	 * Should only be called by {@link PacketListener} and {@link LoginPacketHandler}.
	 *
	 * @param newState the new state of the player
	 */
	public final void setState(@NotNull IodinePlayer.State newState) {
		Validate.isTrue(state != newState, "Old and new state mustn't be the same");
		if (state == State.MODDED) {
			if (openGui != null) {
				openGui.closeForNoPacket(this, true);
			}
			for (IodineOverlayImpl overlay : new ArrayList<>(overlays)) {
				overlay.closeForNoPacket(this, true);
			}
		}
		
		IodinePlayer.State oldState = state;
		state = newState;
		plugin.postEvent(new IodinePlayerStateChangedEvent(this, oldState,newState));
	}
	
	
	
	@Contract(pure = true)
	@Override
	public final IodineGuiImpl getOpenGui() {
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
	public final void setOpenGui(@Nullable IodineGuiImpl gui) {
		Validate.isTrue(openGui == null ^ gui == null, "Opened GUIs must be closed before getting replaced, " +
				"and null values should not follow null values");
		openGui = gui;
	}
	
	@Override
	public final void closeOpenGui() {
		if (openGui != null) {
			openGui.closeFor(this);
		}
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public final Set<IodineOverlay> getOverlays() {
		return Collections.unmodifiableSet(overlays);
	}
	
	/**
	 * Adds an overlay to the internal open-overlays cache.
	 * The overlay must not already be in this cache.
	 * Should only be called by {@link IodineOverlayImpl}.
	 *
	 * @param overlay the newly opened overlay
	 */
	public final void addDisplayedOverlay(@NotNull IodineOverlayImpl overlay) {
		Validate.isTrue(overlays.add(overlay), "Overlay must not be present in cache");
	}
	
	/**
	 * Removes an overlay from the internal open-overlays cache.
	 * The overlay must be in this cache.
	 * Should only be called by {@link IodineOverlayImpl}.
	 *
	 * @param overlay the newly closed overlay
	 */
	public final void removeDisplayedOverlay(@NotNull IodineOverlayImpl overlay) {
		Validate.isTrue(overlays.remove(overlay), "Overlay must be present in cache");
	}
	
	@Override
	public final void closeOverlay(@NotNull IodineOverlay overlay) {
		if (overlay.getViewers().contains(this)) {
			overlay.closeFor(this);
		}
	}
	
	
	
	/**
	 * Throws an exception if the state of this player is not {@link IodinePlayer.State#MODDED}.
	 */
	public final void assertModded() {
		Validate.isTrue(state == IodinePlayer.State.MODDED, "The player's IodinePlayer.State must be 'MODDED'");
	}
}
