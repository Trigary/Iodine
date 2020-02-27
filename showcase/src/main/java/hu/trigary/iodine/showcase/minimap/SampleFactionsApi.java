package hu.trigary.iodine.showcase.minimap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SampleFactionsApi {
	private final Map<String, Faction> factions = new HashMap<>();
	private final Map<ChunkCoordinate, Faction> claims = new HashMap<>();
	
	
	
	@NotNull
	public Faction createFaction(@NotNull String name, int color) {
		if (factions.containsKey(name)) {
			throw new IllegalArgumentException("A faction with the specified name already exists: " + name);
		}
		Faction faction = new Faction(name, color);
		factions.put(name, faction);
		return faction;
	}
	
	@Nullable
	public Faction getFactionAt(@NotNull ChunkCoordinate coordinate) {
		return claims.get(coordinate);
	}
	
	public void setFactionAt(@NotNull ChunkCoordinate coordinate, @Nullable Faction faction) {
		claims.put(coordinate, faction);
	}
}
