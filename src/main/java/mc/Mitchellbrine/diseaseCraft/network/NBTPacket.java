package mc.Mitchellbrine.diseaseCraft.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Mitchellbrine on 2015.
 */
public class NBTPacket implements IMessage, IMessageHandler<NBTPacket,IMessage> {

	private NBTTagCompound compound;
	private String compoundName;
	private int nbtType;

	public NBTPacket(){}

	public NBTPacket(String compoundName, NBTTagCompound compound) {
		this.compoundName = compoundName;
		this.compound = compound;
		this.nbtType = 0;
		//System.out.println(compoundName);
		//System.out.println(compound);
	}

	public NBTPacket(String compoundName, NBTTagCompound compound, int nbtType) {
		this.compoundName = compoundName;
		this.compound = compound;
		this.nbtType = nbtType;
		//System.out.println(compoundName);
		//System.out.println(compound);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		compoundName = ByteBufUtils.readUTF8String(buf);
		compound = ByteBufUtils.readTag(buf);
		nbtType = buf.readInt();
		//System.out.println("Third Check: " + compound);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf,compoundName);
		ByteBufUtils.writeTag(buf, compound);
		buf.writeInt(nbtType);
		//System.out.println("Second check: " + compound);
	}

	@Override
	public IMessage onMessage(NBTPacket message, MessageContext ctx) {
			if (message.compound == null) {
				System.out.println("Compound was null. Wat.");
				return null;
			}
			//System.out.println("Compound wasn't null!");
		switch (nbtType) {
			case 0:
				Minecraft.getMinecraft().thePlayer.getEntityData().setTag(message.compoundName, message.compound);
				break;
			case 1:
				Minecraft.getMinecraft().thePlayer.getEntityData().getCompoundTag("PlayerPersisted").setTag(message.compoundName,message.compound);
		}
		return null;
	}
}
