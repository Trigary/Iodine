package hu.trigary.iodine.server;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.api.IodineEvent;
import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.gui.IodineOverlay;
import hu.trigary.iodine.api.player.IodinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The implementation of {@link IodineApi}.
 */
public final class IodineApiImpl extends IodineApi {
	private final IodinePlugin plugin;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodinePlugin}.
	 *
	 * @param plugin the plugin instance
	 */
	public IodineApiImpl(@NotNull IodinePlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	//TODO docs
	public void installInstance() {
		instance = this;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public IodinePlayer getPlayer(@NotNull UUID player) {
		return plugin.getPlayer(player);
	}
	
	@NotNull
	@Override
	public IodineGui createGui() {
		return plugin.getGuiManager().createGui();
	}
	
	@NotNull
	@Override
	public IodineOverlay createOverlay(@NotNull IodineOverlay.Anchor anchor, int horizontalOffset, int verticalOffset) {
		return plugin.getGuiManager().createOverlay(anchor, horizontalOffset, verticalOffset);
	}
	
	
	
	@Override
	public <T extends IodineEvent> void addListener(@NotNull Class<T> event, @NotNull Consumer<T> handler) {
		plugin.changeEventListener(event, handler, true);
	}
	
	@Override
	public <T extends IodineEvent> void removeListener(@NotNull Class<T> event, @NotNull Consumer<T> handler) {
		plugin.changeEventListener(event, handler, false);
	}
}
