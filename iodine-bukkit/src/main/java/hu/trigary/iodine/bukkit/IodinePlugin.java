package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.bukkit.network.NetworkManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

public class IodinePlugin extends JavaPlugin {
	private static IodinePlugin instance;
	private NetworkManager network;
	
	@Contract("_ -> fail")
	public static void main(String[] args) {
		throw new AssertionError("Fake entry points shouldn't be used");
	}
	
	@Override
	public void onEnable() {
		instance = this;
		network = new NetworkManager();
	}
	
	
	
	public static IodinePlugin getInstance() {
		return instance;
	}
	
	public NetworkManager getNetwork() {
		return network;
	}
}
