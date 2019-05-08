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

public class NetworkManager {
	private final SimpleNetworkWrapper network;
	
	public NetworkManager() {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(IodineConstants.NETWORK_CHANNEL);
		registerOut(PacketType.LOGIN, LoginPacket.class);
		registerIn(PacketType.LOGIN_SUCCESS, LoginSuccessPacket.class, new LoginSuccessPacket.Handler());
		registerIn(PacketType.LOGIN_FAILED, LoginFailedPacket.class, new LoginFailedPacket.Handler());
	}
	
	
	
	public void send(OutPacket packet) {
		IodineMod.getInstance().getLogger().info("Sending packet: " + packet.getClass().getSimpleName());
		network.sendToServer(packet);
	}
	
	
	
	private void registerOut(PacketType type, Class<? extends OutPacket> packet) {
		network.registerMessage(DummyMessageHandler.INSTANCE, packet, type.ordinal(), Side.SERVER);
	}
	
	private <T extends InPacket> void registerIn(PacketType type, Class<T> packet,
			IMessageHandler<T, ? extends OutPacket> handler) {
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
