package hu.trigary.iodine.showcase.minimap;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Faction {
	private final String name;
	private final int color;
	
	@Contract(pure = true)
	public Faction(@NotNull String name, int color) {
		this.name = name;
		this.color = color;
	}
	
	
	
	@Contract(pure = true)
	public String getName() {
		return name;
	}
	
	@Contract(pure = true)
	public int getColor() {
		return color;
	}
}
