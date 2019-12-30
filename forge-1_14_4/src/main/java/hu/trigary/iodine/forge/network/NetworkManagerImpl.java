package hu.trigary.iodine.forge.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.network.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NetworkManagerImpl extends NetworkManager {
	private final ResourceLocation channelName;
	private net.minecraft.network.NetworkManager network;
	
	public NetworkManagerImpl(@NotNull IodineMod mod) {
		super(mod);
		channelName = new ResourceLocation(PacketType.NETWORK_CHANNEL);
		createNetwork(channelName).addListener(this::onNetworkEvent);
	}
	
	
	
	@Override
	public void initialize() {
		//noinspection ConstantConditions,resource
		network = Minecraft.getInstance().getConnection().getNetworkManager();
	}
	
	
	
	@Override
	protected void sendImpl(@NotNull byte[] message) {
		Pair<PacketBuffer, Integer> data = Pair.of(new PacketBuffer(Unpooled.wrappedBuffer(message)), Integer.MIN_VALUE);
		IPacket<?> packet = NetworkDirection.PLAY_TO_SERVER.buildPacket(data, channelName).getThis();
		network.sendPacket(packet);
	}
	
	@NotNull
	private static NetworkInstance createNetwork(@NotNull ResourceLocation channelName) {
		try {
			Method method = NetworkRegistry.class.getDeclaredMethod("createInstance", ResourceLocation.class,
					Supplier.class, Predicate.class, Predicate.class);
			method.setAccessible(true);
			Supplier<String> versionSupplier = () -> NetworkRegistry.ACCEPTVANILLA;
			Predicate<String> versionPredicate = NetworkRegistry.ACCEPTVANILLA::equals;
			return (NetworkInstance) method.invoke(null, channelName, versionSupplier, versionPredicate, versionPredicate);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void onNetworkEvent(@NotNull NetworkEvent event) {
		NetworkEvent.Context context = event.getSource().get();
		context.setPacketHandled(true);
		ServerPlayerEntity sender = context.getSender();
		if (sender != null) {
			getMod().getLogger().warn("Network > Received packet from player {}", sender.getScoreboardName());
			return;
		}
		
		PacketBuffer buffer = event.getPayload();
		byte[] array = new byte[buffer.readableBytes()];
		for (int i = 0; i < array.length; i++) {
			array[i] = buffer.readByte();
		}
		context.enqueueWork(() -> onReceived(ByteBuffer.wrap(array)));
	}
}
