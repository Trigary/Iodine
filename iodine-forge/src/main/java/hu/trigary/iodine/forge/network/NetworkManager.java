package hu.trigary.iodine.forge.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.forge.IodineMod;
import hu.trigary.iodine.forge.network.in.*;
import hu.trigary.iodine.forge.network.out.ClientGuiChangePacket;
import hu.trigary.iodine.forge.network.out.ClientGuiClosePacket;
import hu.trigary.iodine.forge.network.out.ClientLoginPacket;
import hu.trigary.iodine.forge.network.out.OutPacket;
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
		network = NetworkRegistry.INSTANCE.newSimpleChannel(PacketType.NETWORK_CHANNEL);
		
		registerOut(PacketType.CLIENT_LOGIN, ClientLoginPacket.class);
		registerIn(PacketType.SERVER_LOGIN_SUCCESS, ServerLoginSuccessPacket.class,
				new ServerLoginSuccessPacket.Handler(mod));
		registerIn(PacketType.SERVER_LOGIN_FAILED, ServerLoginFailedPacket.class,
				new ServerLoginFailedPacket.Handler(mod));
		registerIn(PacketType.SERVER_LOGIN_REQUEST, ServerLoginRequest.class,
				new ServerLoginRequest.Handler(mod));
		
		registerIn(PacketType.SERVER_GUI_OPEN, ServerGuiOpenPacket.class,
				new ServerGuiOpenPacket.Handler(mod));
		registerIn(PacketType.SERVER_GUI_CLOSE, ServerGuiClosePacket.class,
				new ServerGuiClosePacket.Handler(mod));
		registerIn(PacketType.SERVER_GUI_CHANGE, ServerGuiChangePacket.class,
				new ServerGuiChangePacket.Handler(mod));
		
		registerOut(PacketType.CLIENT_GUI_CLOSE, ClientGuiClosePacket.class);
		registerOut(PacketType.CLIENT_GUI_CHANGE, ClientGuiChangePacket.class);
	}
	
	
	
	public void send(@NotNull OutPacket packet) {
		mod.getLogger().info("Sending packet: " + packet.getClass().getSimpleName());
		network.sendToServer(packet);
	}
	
	
	
	private void registerOut(@NotNull PacketType type, @NotNull Class<? extends OutPacket> packet) {
		network.registerMessage(DummyMessageHandler.INSTANCE, packet, type.getId(), Side.SERVER);
	}
	
	private <T extends InPacket> void registerIn(@NotNull PacketType type, @NotNull Class<T> packet,
			@NotNull IMessageHandler<T, ? extends OutPacket> handler) {
		network.registerMessage(handler, packet, type.getId(), Side.CLIENT);
	}
	
	
	
	private static class DummyMessageHandler implements IMessageHandler<BasePacket, BasePacket> {
		static final DummyMessageHandler INSTANCE = new DummyMessageHandler();
		
		@Override
		public BasePacket onMessage(BasePacket message, MessageContext context) {
			throw new AssertionError("DummyMessageHandler should never receive messages");
		}
	}
}
