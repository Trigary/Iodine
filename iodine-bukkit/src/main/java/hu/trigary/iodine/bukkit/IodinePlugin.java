package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.common.CommonTest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

public class IodinePlugin extends JavaPlugin {
	
	@Contract("_ -> fail")
	public static void main(String[] args) {
		throw new AssertionError("Fake entry points shouldn't be used");
	}
	
	@Override
	public void onEnable() {
		Class<CommonTest> clazz = CommonTest.class;
		Bukkit.broadcastMessage("ping... " + IodineApi.ping());
	}
	
	@Override
	public void onDisable() {
	
	}
}
