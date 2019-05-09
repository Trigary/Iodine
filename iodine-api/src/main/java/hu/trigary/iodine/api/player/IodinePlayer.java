package hu.trigary.iodine.api.player;

import hu.trigary.iodine.api.gui.IodineGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IodinePlayer {
	@NotNull
	@Contract(pure = true)
	public abstract Player getPlayer();
	
	@NotNull
	@Contract(pure = true)
	public abstract PlayerState getState();
	
	@Nullable
	@Contract(pure = true)
	public abstract IodineGui getOpenGui();
	
	
	
	public abstract void openGui(@NotNull IodineGui gui);
	
	public abstract void closeGui();
}
