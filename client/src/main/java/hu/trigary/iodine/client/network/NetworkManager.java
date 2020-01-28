package hu.trigary.iodine.client.network;

import hu.trigary.iodine.backend.InputBuffer;
import hu.trigary.iodine.backend.OutputBuffer;
import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.network.handler.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The manager whose responsibility is sending and receiving packets.
 */
public abstract class NetworkManager {
	private final PacketHandler[] handlers = new PacketHandler[PacketType.getHighestId() + 1];
	private final OutputBuffer outputBuffer = new OutputBuffer();
	private final InputBuffer inputBuffer = new InputBuffer();
	private final IodineMod mod;
	
	/**
	 * Creates a new instance.
	 * Should only be called once, by {@link IodineMod}.
	 *
	 * @param mod the mod instance
	 */
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
	
	
	
	/**
	 * Called every time the client joins the servers and is able to send packets.
	 */
	public abstract void initialize();
	
	
	
	/**
	 * Sends the specified payload to the server.
	 *
	 * @param type the packet type to include
	 * @param dataProvider the callback that writes the data into the buffer
	 */
	public final void send(@NotNull PacketType type, @NotNull Consumer<OutputBuffer> dataProvider) {
		outputBuffer.putByte(type.getId());
		dataProvider.accept(outputBuffer);
		mod.getLogger().debug("Network > sending {}", type);
		sendImpl(outputBuffer.toArrayAndReset());
	}
	
	/**
	 * Sends the specified message to the server.
	 *
	 * @param message the payload to send
	 */
	protected abstract void sendImpl(@NotNull byte[] message);
	
	
	
	/**
	 * Should be called when the client receives a message from the server.
	 *
	 * @param message the received payload.
	 */
	protected final void onReceived(@NotNull byte[] message) {
		if (message.length == 0) {
			mod.getLogger().error("Network > received message with length of 0");
			return;
		}
		
		byte id = message[0];
		PacketType type = PacketType.fromId(id);
		if (type == null) {
			mod.getLogger().error("Network > received invalid type-id {}", id);
			return;
		}
		
		try {
			mod.getLogger().debug("Network > handling {}", type);
			handlers[type.getUnsignedId()].handle(inputBuffer.update(message, 1));
		} catch (Throwable t) {
			mod.getLogger().error("Network > error handling {}", type, t);
		}
	}
}
