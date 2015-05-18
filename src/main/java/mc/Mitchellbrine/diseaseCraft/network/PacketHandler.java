package mc.Mitchellbrine.diseaseCraft.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import mc.Mitchellbrine.diseaseCraft.utils.References;

/**
 * Created by Mitchellbrine on 2015.
 */
public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID);

	public static void init()
	{
		INSTANCE.registerMessage(NBTPacket.class, NBTPacket.class, 0, Side.CLIENT);
	}

}
