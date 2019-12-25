package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utilities which help the chat-based player interaction.
 */
public final class ChatUtils {
	private static final String PREFIX = "§7[§eIodine§7] ";
	
	private ChatUtils() {}
	
	
	
	/**
	 * Formats the specified text as an informational message.
	 *
	 * @param message the message to format
	 * @return the formatted text
	 */
	@NotNull
	@Contract(pure = true)
	public static String formatInfo(@NotNull String message) {
		return PREFIX + "§f" + message;
	}
	
	/**
	 * Formats the specified text as an error message.
	 *
	 * @param message the message to format
	 * @return the formatted text
	 */
	@NotNull
	@Contract(pure = true)
	public static String formatError(@NotNull String message) {
		return PREFIX + "§c" + message;
	}
}
