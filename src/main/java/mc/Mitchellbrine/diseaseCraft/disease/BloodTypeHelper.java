package mc.Mitchellbrine.diseaseCraft.disease;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.network.NBTPacket;
import mc.Mitchellbrine.diseaseCraft.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Mitchellbrine on 2015.
 */
public class BloodTypeHelper {

	private Random random;

	private static Map<String,String> bloodTypes;

	@SubscribeEvent
	public void loadWorld(WorldEvent.Load load) {
		try {
			if (!load.world.isRemote && load.world.provider.dimensionId == 0) {
				File file = new File(load.world.getSaveHandler().getWorldDirectory(), "bloodTypes.txt");
				if (file.exists())
					readFile(file);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SubscribeEvent
	public void saveWorld(WorldEvent.Unload save) {
		if (bloodTypes != null && !save.world.isRemote) {
			createFile(save.world,bloodTypes);
			bloodTypes.clear();
		}
	}

	@SubscribeEvent
	public void setBloodType(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer && !event.world.isRemote) {
			EntityPlayer player = (EntityPlayer)event.entity;
			if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG)) {
				player.getEntityData().setTag(player.PERSISTED_NBT_TAG,new NBTTagCompound());
			}
			if (!player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).hasKey("bloodType")) {
				if (random == null) {
					random = new Random(event.world.getTotalWorldTime());
				} else {
					random.setSeed(event.world.getTotalWorldTime());
				}
				int rand = random.nextInt(101);
				String bloodLetter = "";
				String rhesus;
				if (rand >= 0 && rand < 25) {
					// A blood type (25% chance)
					bloodLetter = "A";
				} else if (rand >= 25 && rand < 50) {
					// B blood type (25% chance)
					bloodLetter = "B";
				} else if (rand >= 50 && rand < 90) {
					// O blood type (40% chance)
					bloodLetter = "O";
				} else if (rand >= 90) {
					// AB blood type (10% chance)
					bloodLetter = "AB";
				}
				int random2 = random.nextInt(101);
				if (random2 < 75) {
					rhesus = "+";
				} else {
					rhesus = "-";
				}
				player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setString("bloodType", bloodLetter + rhesus);
				System.out.println(player.getCommandSenderName() + " | Blood Type: " + player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getString("bloodType"));
				if (bloodTypes == null) {
					bloodTypes = new HashMap<String, String>();
				}
				bloodTypes.put(player.getCommandSenderName(),player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getString("bloodType"));
				if (!player.worldObj.isRemote) {
					PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG, player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)), (EntityPlayerMP) player.worldObj.getClosestPlayer(player.posX, player.posY, player.posZ, 10));
				}
			} else {
				if (bloodTypes != null) {
					if (!bloodTypes.containsKey(player.getCommandSenderName())) {
						bloodTypes.put(player.getCommandSenderName(),player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getString("bloodType"));
					}
				} else {
					bloodTypes = new HashMap<String, String>();
					bloodTypes.put(player.getCommandSenderName(),player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getString("bloodType"));
				}
				System.out.println(bloodTypes.get(player.getCommandSenderName()));
			}
		}
	}

	private void createFile(World world, Map<String, String> map) {
		if (world.isRemote || map == null) {
			return;
		}
		if (world.provider.dimensionId != 0)
			return;
		File file = new File(world.getSaveHandler().getWorldDirectory(),"bloodTypes.txt");

		try {
			/*BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			if (reader.readLine() != null) {
				reader.close();
				return;
			} */

			PrintWriter writer = new PrintWriter(file);
			for (String string : map.keySet()) {
				writer.println(string + ":" + map.get(string));
			}
			writer.close();
			DiseaseCraft.logger.info("Finished writing the file");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void readFile(File file) throws IOException,ParseException {
		if (!file.exists())
			throw new FileNotFoundException("File " + file.getName() + " does not exist, so blood types cannot be read");

		if (bloodTypes == null) {
			bloodTypes = new HashMap<String, String>();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String s;

		while ((s = reader.readLine()) != null) {
			if (!s.contains(":"))
				throw new ParseException("Missing colon in line, this is not supposed to happen! Cannot read blood types",-1);
			String player = s.substring(0,s.indexOf(':'));
			String bloodType = s.substring(s.indexOf(':') + 1);
			System.out.println("Player: " + player + " | Blood Type: " + bloodType);
			bloodTypes.put(player,bloodType);
		}

		reader.close();

		DiseaseCraft.logger.info("Loaded " + bloodTypes.size() + " players' blood types");
	}

	public static String getBloodType(String name) {
		return bloodTypes.containsKey(name) ? bloodTypes.get(name) : "missingno";
	}

	public static boolean isCompatible(String donor, String receiver) {
		if (donor.equalsIgnoreCase("O-")) {
			return true;
		} else if (donor.equalsIgnoreCase("O+")) {
			return receiver.endsWith("+");
		} else if (donor.equalsIgnoreCase("B-")) {
			return receiver.contains("B");
		} else if (donor.equalsIgnoreCase("B+")) {
			return receiver.contains("B") && receiver.endsWith("+");
		} else if (donor.equalsIgnoreCase("A-")) {
			return receiver.contains("A");
		} else if (donor.equalsIgnoreCase("A+")) {
			return receiver.contains("A") && receiver.endsWith("+");
		} else if (donor.equalsIgnoreCase("AB-")) {
			return receiver.contains("AB");
		} else {
			return receiver.equals(donor);
		}
	}

	public static boolean isCompatible(EntityPlayer donor, EntityPlayer receiver) {
		return isCompatible(donor.getEntityData().getCompoundTag(donor.PERSISTED_NBT_TAG).getString("bloodType"),receiver.getEntityData().getCompoundTag(receiver.PERSISTED_NBT_TAG).getString("bloodType"));
	}

}
