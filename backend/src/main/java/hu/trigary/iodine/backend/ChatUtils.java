package hu.trigary.iodine.backend;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utilities which help the chat-based player interaction.
 */
public final class ChatUtils {
	private static final String PREFIX = "\u00a77[\u00a7eIodine\u00a77] ";
	
	private ChatUtils() {}
	
	
	
	/**
	 * Formats the specified text as an informational message.
	 *
	 * @param message the message to format
	 * @return the formatted text
	 */
	@NotNull
	@Contract(pure = true)
	public static String[] formatInfo(@NotNull String... message) {
		return format("\u00a7f", message);
	}
	
	/**
	 * Formats the specified text as an error message.
	 *
	 * @param message the message to format
	 * @return the formatted text
	 */
	@NotNull
	@Contract(pure = true)
	public static String[] formatError(@NotNull String... message) {
		return format("\u00a7c", message);
	}
	
	@NotNull
	@Contract(pure = true)
	private static String[] format(@NotNull String color, @NotNull String... message) {
		String[] result = new String[message.length];
		if (result.length > 0) {
			result[0] = PREFIX + color + message[0];
			if (result.length > 1) {
				for (int i = 1; i < result.length; i++) {
					result[i] = color + message[i];
				}
			}
		}
		return result;
	}
}
