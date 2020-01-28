package hu.trigary.iodine.forge.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.network.NetworkManager;
import hu.trigary.iodine.forge.IodineModImpl;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * The implementation of {@link NetworkManager}.
 */
public class NetworkManagerImpl extends NetworkManager {
	private final IodineModImpl mod;
	private final ResourceLocation channelName;
	private net.minecraft.network.NetworkManager network;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
	public NetworkManagerImpl(@NotNull IodineModImpl mod) {
		super(mod);
		this.mod = mod;
		channelName = new ResourceLocation(PacketType.NETWORK_CHANNEL);
		Predicate<String> versionPredicate = NetworkRegistry.ACCEPTVANILLA::equals;
		NetworkRegistry.newEventChannel(channelName, () -> NetworkRegistry.ACCEPTVANILLA,
				versionPredicate, versionPredicate).addListener(this::onNetworkEvent);
	}
	
	
	
	@Override
	public void initialize() {
		//noinspection ConstantConditions
		network = Minecraft.getInstance().getConnection().getNetworkManager();
	}
	
	
	
	@Override
	protected void sendImpl(@NotNull byte[] message) {
		Pair<PacketBuffer, Integer> data = Pair.of(new PacketBuffer(Unpooled.wrappedBuffer(message)), Integer.MIN_VALUE);
		IPacket<?> packet = NetworkDirection.PLAY_TO_SERVER.buildPacket(data, channelName).getThis();
		network.sendPacket(packet);
	}
	
	private void onNetworkEvent(@NotNull NetworkEvent event) {
		NetworkEvent.Context context = event.getSource().get();
		context.setPacketHandled(true);
		ServerPlayerEntity sender = context.getSender();
		if (sender != null) {
			mod.getLogger().warn("Network > Received packet from player {}", sender.getScoreboardName());
			return;
		}
		
		PacketBuffer buffer = event.getPayload();
		byte[] array = new byte[buffer.readableBytes()];
		if (buffer.hasArray()) {
			System.arraycopy(buffer.array(), buffer.arrayOffset(), array, 0, array.length);
		} else {
			for (int i = 0; i < array.length; i++) {
				array[i] = buffer.readByte();
			}
		}
		context.enqueueWork(() -> onReceived(array));
	}
}
