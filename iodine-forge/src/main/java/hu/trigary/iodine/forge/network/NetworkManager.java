package hu.trigary.iodine.forge.network;

import hu.trigary.iodine.common.IodineConstants;
import hu.trigary.iodine.common.PacketType;
import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.packet.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

public class NetworkManager {
	private final IodineMod mod;
	private final SimpleNetworkWrapper network;
	
	public NetworkManager(@NotNull IodineMod mod) {
		this.mod = mod;
		network = NetworkRegistry.INSTANCE.newSimpleChannel(IodineConstants.NETWORK_CHANNEL);
		
		registerOut(PacketType.LOGIN, LoginPacket.class);
		registerIn(PacketType.LOGIN_SUCCESS, LoginSuccessPacket.class, new LoginSuccessPacket.Handler(mod));
		registerIn(PacketType.LOGIN_FAILED, LoginFailedPacket.class, new LoginFailedPacket.Handler(mod));
	}
	
	
	
	public void send(@NotNull OutPacket packet) {
		mod.getLogger().info("Sending packet: " + packet.getClass().getSimpleName());
		network.sendToServer(packet);
	}
	
	
	
	private void registerOut(@NotNull PacketType type, @NotNull Class<? extends OutPacket> packet) {
		network.registerMessage(DummyMessageHandler.INSTANCE, packet, type.ordinal(), Side.SERVER);
	}
	
	private <T extends InPacket> void registerIn(@NotNull PacketType type, @NotNull Class<T> packet,
			@NotNull IMessageHandler<T, ? extends OutPacket> handler) {
		network.registerMessage(handler, packet, type.ordinal(), Side.CLIENT);
	}
	
	
	
	private static class DummyMessageHandler implements IMessageHandler<BasePacket, BasePacket> {
		static final DummyMessageHandler INSTANCE = new DummyMessageHandler();
		
		@Override
		public BasePacket onMessage(BasePacket message, MessageContext context) {
			throw new AssertionError("DummyMessageHandler should never receive messages");
		}
	}
}
