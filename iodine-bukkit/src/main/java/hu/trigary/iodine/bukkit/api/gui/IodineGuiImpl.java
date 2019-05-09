package hu.trigary.iodine.bukkit.api.gui;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.IodinePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class IodineGuiImpl extends IodineGui {
	private final Set<Player> viewers = new HashSet<>();
	private final IodinePlugin plugin;
	private final int id;
	private Consumer<Player> openAction;
	private Consumer<Player> closeAction;
	
	public IodineGuiImpl(@NotNull IodinePlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	
	
	@Contract(pure = true)
	public int getId() {
		return id;
	}
	
	@NotNull
	@Override
	public Set<Player> getViewers() {
		return Collections.unmodifiableSet(viewers);
	}
	
	
	
	@Override
	public void setOpenAction(@Nullable Consumer<Player> action) {
		openAction = action;
	}
	
	@Override
	public void setCloseAction(@Nullable Consumer<Player> action) {
		closeAction = action;
	}
	
	
	
	@Override
	public void openFor(@NotNull Player player) {
		if (!viewers.add(player)) {
			return;
		}
		
		if (openAction != null) {
			openAction.accept(player);
		}
		
		if (viewers.size() == 1) {
			plugin.getGui().rememberGui(this);
		}
		
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_OPEN, id);
		//TODO serialize the GUI
	}
	
	@Override
	public void closeFor(@NotNull Player player) {
		closedBy(player);
		plugin.getNetwork().send(player, PacketType.SERVER_GUI_CLOSE, 4, buffer -> buffer.putInt(id));
	}
	
	public void closedBy(@NotNull Player player) {
		if (!viewers.remove(player)) {
			return;
		}
		
		if (closeAction != null) {
			closeAction.accept(player);
		}
		
		if (viewers.isEmpty()) {
			plugin.getGui().forgetGui(this);
		}
	}
}
