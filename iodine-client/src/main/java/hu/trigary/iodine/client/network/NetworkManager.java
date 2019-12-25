package hu.trigary.iodine.client.network;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineModBase;
import hu.trigary.iodine.client.network.handler.*;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class NetworkManager {
	protected final IodineModBase mod;
	private final PacketHandler[] handlers = new PacketHandler[PacketType.getHighestId() + 1];
	
	protected NetworkManager(@NotNull IodineModBase mod) {
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
	
	
	
	public void send(@NotNull PacketType type, int contentsLength, @NotNull Consumer<ByteBuffer> contentProvider) {
		byte[] message = new byte[contentsLength];
		contentProvider.accept(ByteBuffer.wrap(message));
		mod.getLogger().info("Sending packet: " + type);
		sendImpl(type, message);
	}
	
	protected abstract void sendImpl(@NotNull PacketType type, @NotNull byte[] message);
	
	
	
	//TODO for simplicity just call this method on the main thread
	protected void onReceived(@NotNull PacketType type, @NotNull byte[] message) {
		try {
			handlers[type.getUnsignedId()].handle(ByteBuffer.wrap(message));
			mod.getLogger().info("Successfully handled received packet: " + type);
		} catch (Throwable t) {
			mod.getLogger().log(Level.WARNING, "Error handling received packet: " + type, t);
		}
	}
}
