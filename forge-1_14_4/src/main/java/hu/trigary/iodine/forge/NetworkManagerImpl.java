package hu.trigary.iodine.forge;

import hu.trigary.iodine.backend.PacketType;
import hu.trigary.iodine.client.IodineMod;
import hu.trigary.iodine.client.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public class NetworkManagerImpl extends NetworkManager {
	public NetworkManagerImpl(@NotNull IodineMod mod) {
		super(mod);
	}



	@Override
	protected void sendImpl(@NotNull PacketType type, @NotNull byte[] message) {
		//TODO please tell me what loginIndexGetter is in IndexMessageCodec
		// so I don't have to create a separate class for all packet types
	}


	
	//TODO call onReceived somewhere somehow (preferably no black magic)
}
