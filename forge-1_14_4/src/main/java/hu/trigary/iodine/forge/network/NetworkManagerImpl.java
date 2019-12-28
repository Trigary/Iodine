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
	
	public NetworkManagerImpl(@NotNull IodineMod mod) {
		super(mod);
		channelName = new ResourceLocation(PacketType.NETWORK_CHANNEL);
		NetworkInstance network = createNetwork(mod::getVersion, channelName);
		network.addListener(this::onNetworkEvent);
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
	
	@Override
	protected void sendImpl(@NotNull byte[] message) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.wrappedBuffer(message));
		IPacket<?> packet = NetworkDirection.PLAY_TO_SERVER.buildPacket(Pair.of(buffer, Integer.MIN_VALUE), channelName).getThis();
		//noinspection ConstantConditions,resource
		Minecraft.getInstance().getConnection().getNetworkManager().sendPacket(packet);
	}
	
	private void onNetworkEvent(@NotNull NetworkEvent event) {
		NetworkEvent.Context context = event.getSource().get();
		ServerPlayerEntity sender = context.getSender();
		if (sender == null) {
			context.enqueueWork(() -> onReceived(ByteBuffer.wrap(event.getPayload().array())));
		} else {
			mod.getLogger().warning("Received packet from another player: " + sender.getScoreboardName());
		}
	}
}
