package hu.trigary.iodine.client.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.network.handler.*;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class NetworkManager {
	protected final IodineMod mod;
	private final PacketHandler[] handlers = new PacketHandler[PacketType.getHighestId() + 1];
	
	protected NetworkManager(@NotNull IodineMod mod) {
		this.mod = mod;
		handlers[PacketType.SERVER_LOGIN_SUCCESS.getUnsignedId()] = new LoginSuccessPacketHandler(mod);
		handlers[PacketType.SERVER_LOGIN_FAILED.getUnsignedId()] = new LoginFailedPacketHandler(mod);
		handlers[PacketType.SERVER_LOGIN_REQUEST.getUnsignedId()] = new LoginRequestPacketHandler(mod);
		handlers[PacketType.SERVER_GUI_OPEN.getUnsignedId()] = new GuiOpenPacketHandler(mod);
		handlers[PacketType.SERVER_OVERLAY_OPEN.getUnsignedId()] = new OverlayOpenPacketHandler(mod);
		handlers[PacketType.SERVER_GUI_CHANGE.getUnsignedId()] = new GuiChangePacketHandler(mod);
		handlers[PacketType.SERVER_OVERLAY_CHANGE.getUnsignedId()] = new OverlayChangePacketHandler(mod);
		handlers[PacketType.SERVER_GUI_CLOSE.getUnsignedId()] = new GuiClosePacketHandler(mod);
		handlers[PacketType.SERVER_OVERLAY_CLOSE.getUnsignedId()] = new OverlayClosePacketHandler(mod);
	}
	
	
	
	public final void send(@NotNull PacketType type, int dataLength, @NotNull Consumer<ByteBuffer> dataProvider) {
		byte[] message = new byte[dataLength + 1];
		message[0] = type.getId();
		dataProvider.accept(ByteBuffer.wrap(message, 1, dataLength));
		mod.getLogger().info("Sending packet: " + type);
		sendImpl(message);
	}
	
	protected abstract void sendImpl(@NotNull byte[] message);
	
	
	
	//TODO docs: call this method on the main thread
	protected final void onReceived(@NotNull ByteBuffer message) {
		byte id = message.get();
		PacketType type = PacketType.fromId(id);
		if (type == null) {
			mod.getLogger().severe("Received message with invalid type-id: " + id);
			return;
		}
		
		try {
			handlers[type.getUnsignedId()].handle(message);
			mod.getLogger().info("Successfully handled received packet: " + type);
		} catch (Throwable t) {
			mod.getLogger().log(Level.SEVERE, "Error handling received packet: " + type, t);
		}
	}
}
