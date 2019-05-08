package hu.trigary.iodine.bukkit.network.handler;

import com.google.common.base.Charsets;
import hu.trigary.iodine.bukkit.IodinePlugin;
import hu.trigary.iodine.common.PacketType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LoginPacketHandler implements PacketHandler {
	private final Pattern versionPattern = Pattern.compile("\\d+.\\d+.\\d+");
	private final String serverVersion;
	private final int serverMajor;
	private final int serverMinor;
	
	public LoginPacketHandler() {
		serverVersion = IodinePlugin.getInstance().getDescription().getVersion();
		Validate.isTrue(versionPattern.matcher(serverVersion).matches(), "Version must match the #.#.# format");
		int[] raw = Arrays.stream(StringUtils.split(serverVersion, '.'))
				.limit(2).mapToInt(Integer::parseInt).toArray();
		serverMajor = raw[0];
		serverMinor = raw[1];
	}
	
	
	
	@Override
	public void handle(Player player, byte[] message) {
		String clientVersion = new String(message, 1, message.length - 1, Charsets.UTF_8);
		if (serverVersion.equals(clientVersion) || clientVersion.equals("${version}")) {
			//TODO remove this as soon as placeholders start getting replaced when launching MC from IDE
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
	
	
	
	private void versionMatches(Player player) { //TODO log
		IodinePlugin.getInstance().getNetwork().send(player, PacketType.LOGIN_SUCCESS);
	}
	
	private void outdatedParty(Player player, boolean outdatedClient) { //TODO log
		IodinePlugin.getInstance().getNetwork().send(player,
				PacketType.LOGIN_FAILED, outdatedClient ? (byte) 0 : 1);
	}
	
	private void unexpectedInput(Player player) {
		outdatedParty(player, true); //TODO log
	}
}
