package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * The handler of {@link PacketType#CLIENT_LOGIN}.
 */
public class LoginPacketHandler extends PacketHandler {
	private final Pattern versionPattern = Pattern.compile("\\d+\\.\\d+\\.\\d+");
	private final String serverVersion;
	private final int serverMajor;
	private final int serverMinor;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link PacketListener}.
	 *
	 * @param plugin the plugin instance
	 */
	public LoginPacketHandler(@NotNull IodinePlugin plugin) {
		super(plugin);
		
		serverVersion = plugin.getDescription().getVersion();
		Validate.isTrue(versionPattern.matcher(serverVersion).matches(), "Version must match the #.#.# format");
		
		int[] raw = Arrays.stream(StringUtils.split(serverVersion, '.'))
				.limit(2).mapToInt(Integer::parseInt).toArray();
		serverMajor = raw[0];
		serverMinor = raw[1];
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	@Override
	public IodinePlayer.State getTargetState() {
		return IodinePlayer.State.VANILLA;
	}
	
	@Override
	public void handle(@NotNull IodinePlayerImpl player, @NotNull ByteBuffer message) {
		byte[] array = message.array(); //backing array's first element is the PacketType
		String clientVersion = new String(array, 1, array.length - 1, StandardCharsets.UTF_8);
		
		if (serverVersion.equals(clientVersion) || clientVersion.equals("${version}") || clientVersion.equals("version")) {
			//Forge is weird... let's call this a feature, not like this affects anyone apart from the developers
			versionMatches(player);
			return;
		}
		
		if (!versionPattern.matcher(clientVersion).matches()) {
			unexpectedInput(player);
			return;
		}
		
		String[] split = StringUtils.split(clientVersion, '.');
		if (split.length != 3) {
			unexpectedInput(player);
			return;
		}
		
		int clientMajor = Integer.parseInt(split[0]);
		int clientMinor = Integer.parseInt(split[1]);
		if (clientMajor < serverMajor) {
			outdatedParty(player, true);
		} else if (clientMajor > serverMinor) {
			outdatedParty(player, false);
		} else if (clientMinor < serverMinor) {
			outdatedParty(player, true);
		} else if (clientMinor > serverMinor) {
			outdatedParty(player, false);
		} else {
			versionMatches(player);
		}
	}
	
	
	
	private void unexpectedInput(@NotNull IodinePlayerImpl player) {
		plugin.log(Level.INFO, "Login failed for: {0} (invalid LoginPacket)", player.getPlayer().getName());
		player.setState(IodinePlayer.State.INVALID);
		plugin.getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_FAILED, (byte) 0);
	}
	
	private void outdatedParty(@NotNull IodinePlayerImpl player, boolean outdatedClient) {
		plugin.log(Level.INFO, "Login failed for: {0} (outdated {1})",
				player.getPlayer().getName(), outdatedClient ? "client" : "server");
		player.setState(IodinePlayer.State.INVALID);
		plugin.getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_FAILED, outdatedClient ? (byte) 0 : 1);
	}
	
	private void versionMatches(@NotNull IodinePlayerImpl player) {
		plugin.log(Level.INFO, "Logged in successfully: {0}", player.getPlayer().getName());
		player.setState(IodinePlayer.State.MODDED);
		plugin.getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_SUCCESS);
	}
}
