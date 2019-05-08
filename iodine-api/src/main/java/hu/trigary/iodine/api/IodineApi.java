package hu.trigary.iodine.api;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class IodineApi {
	
	@NotNull
	public static String ping() {
		Bukkit.getPluginManager(); //to test whether Bukkit is present
		return "pong";
	}
}
