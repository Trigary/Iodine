package hu.trigary.iodine.showcase.minimap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChunkCoordinate {
	private final int x;
	private final int z;
	
	@Contract(pure = true)
	private ChunkCoordinate(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public static ChunkCoordinate fromChunkCoords(int x, int z) {
		return new ChunkCoordinate(x, z);
	}
	
	@NotNull
	@Contract(pure = true)
	public static ChunkCoordinate fromNormalCoords(int x, int z) {
		return new ChunkCoordinate(x >> 4, z >> 4);
	}
	
	@NotNull
	@Contract(pure = true)
	public static ChunkCoordinate fromLocation(@NotNull Location location) {
		return new ChunkCoordinate(location.getBlockX() >> 4, location.getBlockZ() >> 4);
	}
	
	@NotNull
	@Contract(pure = true)
	public static ChunkCoordinate fromChunk(@NotNull Chunk chunk) {
		return new ChunkCoordinate(chunk.getX(), chunk.getZ());
	}
	
	
	
	@Contract(pure = true)
	public int getX() {
		return x;
	}
	
	@Contract(pure = true)
	public int getZ() {
		return z;
	}
	
	
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ChunkCoordinate)) {
			return false;
		}
		
		ChunkCoordinate other = (ChunkCoordinate) object;
		return other.x == x && other.z == z;
	}
	
	@Override
	public int hashCode() {
		return 31 * x + z;
	}
}
