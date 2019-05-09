package hu.trigary.iodine.bukkit;

import hu.trigary.iodine.api.IodineApi;
import hu.trigary.iodine.bukkit.api.IodineApiImpl;
import hu.trigary.iodine.bukkit.api.player.IodinePlayerImpl;
import hu.trigary.iodine.bukkit.network.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class IodinePlugin extends JavaPlugin {
	private static final boolean DEBUG_LOG = true;
	private NetworkManager networkManager;
	private PlayerManager playerManager;
	private GuiManager guiManager;
	
	@Contract("_ -> fail")
	public static void main(String[] args) {
		throw new AssertionError("Fake entry points shouldn't be used");
	}
	
	@Override
	public void onEnable() {
		IodineApiImpl api = new IodineApiImpl(this);
		Bukkit.getServicesManager().register(IodineApi.class, api, this, ServicePriority.Normal);
		
		networkManager = new NetworkManager(this);
		playerManager = new PlayerManager(this);
		guiManager = new GuiManager(this);
		
		Bukkit.getPluginManager().registerEvents(new TestCommandListener(), this);
	}
	
	@Override
	public void onDisable() {
		//TODO close all GUIs, as late as possible (so other plugins' onDisable can still act upon them)
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public NetworkManager getNetwork() {
		return networkManager;
	}
	
	@NotNull
	@Contract(pure = true)
	public IodinePlayerImpl getPlayer(@NotNull Player player) {
		return playerManager.getPlayer(player);
	}
	
	@NotNull
	@Contract(pure = true)
	public GuiManager getGui() {
		return guiManager;
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
