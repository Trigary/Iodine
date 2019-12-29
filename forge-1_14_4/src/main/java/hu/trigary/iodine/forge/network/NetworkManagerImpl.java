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
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkInstance;
import net.minecraftforge.fml.network.NetworkRegistry;
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
	}
	
	
	
	@Override
	public void initialize() {
		//noinspection ConstantConditions,resource
		network = Minecraft.getInstance().getConnection().getNetworkManager();
		createNetwork(mod::getVersion, channelName).addListener(this::onNetworkEvent);
	}
	
	
	
	@Override
	protected void sendImpl(@NotNull byte[] message) {
		Pair<PacketBuffer, Integer> data = Pair.of(new PacketBuffer(Unpooled.wrappedBuffer(message)), Integer.MIN_VALUE);
		IPacket<?> packet = NetworkDirection.PLAY_TO_SERVER.buildPacket(data, channelName).getThis();
		network.sendPacket(packet);
	}
	
	@NotNull
	private static NetworkInstance createNetwork(@NotNull Supplier<String> versionSupplier, @NotNull ResourceLocation channelName) {
		try {
			Method method = NetworkRegistry.class.getDeclaredMethod("createInstance", ResourceLocation.class,
					Supplier.class, Predicate.class, Predicate.class);
			method.setAccessible(true);
			Predicate<String> versionPredicate = NetworkRegistry.ABSENT::equals;
			return (NetworkInstance) method.invoke(null, channelName, versionSupplier, versionPredicate, versionPredicate);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void onNetworkEvent(@NotNull NetworkEvent event) {
		NetworkEvent.Context context = event.getSource().get();
		ServerPlayerEntity sender = context.getSender();
		if (sender == null) {
			context.enqueueWork(() -> onReceived(ByteBuffer.wrap(event.getPayload().array())));
		} else {
			mod.getLogger().warn("Received packet from another player: " + sender.getScoreboardName());
		}
	}
}
