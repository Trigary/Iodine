package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.bukkit.api.IodineApiImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class IodinePlugin extends JavaPlugin {
	private static final boolean DEBUG_LOG = true;
	private NetworkManager network;
	private PlayerManager player;
	
	@Contract("_ -> fail")
	public static void main(String[] args) {
		throw new AssertionError("Fake entry points shouldn't be used");
	}
	
	@Override
	public void onEnable() {
		IodineApiImpl api = new IodineApiImpl(this);
		Bukkit.getServicesManager().register(IodineApi.class, api, this, ServicePriority.Normal);
		
		network = new NetworkManager(this);
		player = new PlayerManager(this);
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public NetworkManager getNetwork() {
		return network;
	}
	
	@NotNull
	@Contract(pure = true)
	public PlayerManager getPlayer() {
		return player;
	}
	
	
	
	public void logDebug(String message, Object... params) {
		if (DEBUG_LOG) {
			getLogger().log(Level.INFO, message, params);
		}
	}
	
	public void logDebug(String message, Throwable cause) {
		if (DEBUG_LOG) {
			getLogger().log(Level.INFO, message, cause);
		}
	}
}
