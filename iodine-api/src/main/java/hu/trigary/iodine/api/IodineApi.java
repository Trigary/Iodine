package hu.trigary.iodine.api;

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
	public abstract PlayerState getState(@NotNull Player player);
}
