package hu.trigary.iodine.bukkit.api.gui;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.bukkit.GuiManager;
import hu.trigary.iodine.bukkit.api.player.IodinePlayerImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * The implementation of {@link IodineGui}.
 */
public class IodineGuiImpl extends IodineGui {
	private final Set<Player> viewers = new HashSet<>();
	private final IodinePlugin plugin;
	private final int id;
	private BiConsumer<IodineGui, Player> openAction;
	private BiConsumer<IodineGui, Player> closeAction;
	
	/**
	 * Creates a new instance.
	 * Should only be called by {@link GuiManager}.
	 *
	 * @param plugin the plugin instance
	 * @param id the unique identifier of this GUI instance
	 */
	public IodineGuiImpl(@NotNull IodinePlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	
	
	/**
	 * Gets this GUI's unique identifier.
	 *
	 * @return the ID of this GUI
	 */
	@Contract(pure = true)
	public int getId() {
		return id;
	}
	
	@NotNull
	@Contract(pure = true)
	@Override
	public Collection<Player> getViewers() {
		return Collections.unmodifiableCollection(viewers);
	}
	
	
	
	@Override
	public IodineGui setOpenAction(@Nullable BiConsumer<IodineGui, Player> action) {
		openAction = action;
		return this;
	}
	
	@Override
	public IodineGui setCloseAction(@Nullable BiConsumer<IodineGui, Player> action) {
		closeAction = action;
		return this;
	}
	
	
	
	@Override
	public IodineGui openFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		
		IodineGuiImpl previous = iodinePlayer.getOpenGui();
		if (previous == this) {
			return this;
		} else if (previous != null) {
			previous.closeForNoPacket(iodinePlayer);
		}
		
		viewers.add(player);
		if (viewers.size() == 1) {
			plugin.getGui().remember(this);
		}
		
		iodinePlayer.setOpenGui(this);
		if (openAction != null) {
			openAction.accept(this, player);
		}
		
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_OPEN, id);
		//TODO serialize the GUI
		return this;
	}
	
	@Override
	public IodineGui closeFor(@NotNull Player player) {
		IodinePlayerImpl iodinePlayer = plugin.getPlayer(player);
		iodinePlayer.assertModded();
		closeForNoPacket(iodinePlayer);
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_CLOSE, 4, buffer -> buffer.putInt(id));
		return this;
	}
	
	/**
	 * Closes this GUI for the specified player.
	 * This method differs from {@link #closeFor(Player)} in that a packet
	 * is not sent to the client instructing it to close the GUI.
	 *
	 * @param iodinePlayer the target player
	 */
	public void closeForNoPacket(@NotNull IodinePlayerImpl iodinePlayer) {
		Player player = iodinePlayer.getPlayer();
		if (!viewers.remove(player)) {
			return;
		}
		
		if (viewers.isEmpty()) {
			plugin.getGui().forget(this);
		}
		
		iodinePlayer.setOpenGui(null);
		if (closeAction != null) {
			closeAction.accept(this, player);
		}
	}
}
