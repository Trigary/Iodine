package hu.trigary.iodine.client.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Validator {
	private Validator() {}
	
	
	
	@Contract(pure = true)
	public static void notNull(@Nullable Object value, @NotNull String message) {
		isTrue(value != null, message);
	}
	
	@Contract(pure = true)
	public static void isTrue(boolean value, @NotNull String message) {
		if (!value) {
			throw new AssertionError(message);
		}
	}
}
