package hu.trigary.iodine.client.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Validator {
	private Validator() {}
	
	
	
	public static void notNull(@Nullable Object value, @NotNull String message) {
		isTrue(value != null, message);
	}
	
	public static void isTrue(boolean value, @NotNull String message) {
		if (!value) {
			throw new AssertionError(message);
		}
	}
}
