package hu.trigary.iodine.bukkit.network.handler;

import hu.trigary.iodine.api.player.IodinePlayer;
import hu.trigary.iodine.backend.BufferUtils;
import hu.trigary.iodine.backend.ChatUtils;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.bukkit.network.PacketListener;
import hu.trigary.iodine.bukkit.player.IodinePlayerImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
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
		String clientVersion = BufferUtils.deserializeString(message, 50);
		if (clientVersion == null) {
			getPlugin().getLogger().log(Level.OFF, "Login > received too long version");
			unexpectedInput(player);
			return;
		}
		
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
		getPlugin().log(Level.INFO, "Login > failed for {0} (invalid packet)", player.getPlayer().getName());
		player.setState(IodinePlayer.State.INVALID);
		getPlugin().getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_FAILED, (byte) 0);
		player.getPlayer().sendMessage(ChatUtils.formatError("Iodine handshake failed",
				"Mod features have been disabled for this session.",
				"The handshake has failed due to an invalid packet format.",
				"Unable to determine which party is outdated.",
				"If you believe this is a bug, please report it."));
	}
	
	private void outdatedParty(@NotNull IodinePlayerImpl player, boolean outdatedClient) {
		getPlugin().log(Level.INFO, "Login > failed for {0} (outdated {1})",
				player.getPlayer().getName(), outdatedClient ? "client" : "server");
		player.setState(IodinePlayer.State.INVALID);
		getPlugin().getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_FAILED, outdatedClient ? (byte) 1 : 2);
		String outdated = outdatedClient
				? "Your client is outdated, please follow the server and update."
				: "The server is outdated, consider asking them to update.";
		player.getPlayer().sendMessage(ChatUtils.formatError("Iodine handshake failed",
				"Mod features have been disabled for this session.",
				"The handshake has failed due to a mod version mismatch.",
				outdated));
	}
	
	private void versionMatches(@NotNull IodinePlayerImpl player) {
		getPlugin().log(Level.INFO, "Login > successful for {0}", player.getPlayer().getName());
		player.setState(IodinePlayer.State.MODDED);
		getPlugin().getNetwork().send(player.getPlayer(), PacketType.SERVER_LOGIN_SUCCESS);
	}
}
