package hu.trigary.iodine.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public abstract class IodineGui {
	@NotNull
	@Contract(pure = true)
	public abstract Set<Player> getViewers();
	
	
	public abstract void setOpenAction(@Nullable Consumer<Player> action);
	
	public abstract void setCloseAction(@Nullable  Consumer<Player> action);
	
	
	
	public abstract void openFor(@NotNull Player player);
	
	public abstract void closeFor(@NotNull Player player);
}
