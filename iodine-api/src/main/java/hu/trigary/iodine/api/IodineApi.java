package hu.trigary.iodine.api;

import hu.trigary.iodine.api.gui.IodineGui;
import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.api.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class IodineApi {
	
	@NotNull
	@Contract(pure = true)
	public static IodineApi getInstance() {
		return Bukkit.getServicesManager().getRegistration(IodineApi.class).getProvider();
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public abstract IodinePlayer getPlayer(@NotNull Player player);
	
	@Contract(pure = true)
	public boolean isModded(@NotNull Player player) {
		return getPlayer(player).getState() == PlayerState.MODDED;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public abstract IodineGui createGui();
}
