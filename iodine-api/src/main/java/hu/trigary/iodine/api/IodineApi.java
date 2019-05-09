package hu.trigary.iodine.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class IodineApi {
	protected static IodineApi instance;
	
	@NotNull
	@Contract(pure = true)
	public static IodineApi getInstance() {
		return instance;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public abstract PlayerState getState(@NotNull Player player);
}
