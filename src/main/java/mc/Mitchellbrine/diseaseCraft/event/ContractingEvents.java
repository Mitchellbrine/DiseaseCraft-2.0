package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.JsonPrimitive;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameData;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.dio.DCVersion;
import mc.Mitchellbrine.diseaseCraft.dio.VersionJSON;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import mc.Mitchellbrine.diseaseCraft.network.NBTPacket;
import mc.Mitchellbrine.diseaseCraft.network.PacketHandler;
import mc.Mitchellbrine.diseaseCraft.utils.StatHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ContractingEvents {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			for (Disease disease : Diseases.diseases) {
				if (event.source != null && event.source.getEntity() != null && EntityList.classToStringMapping != null && disease.getWaysToContract() != null && EntityList.getEntityString(event.source.getEntity()) != null && disease.getParameters("mobAttack") != null && disease.getWaysToContract().contains("mobAttack") && ((JsonPrimitive) disease.getParameters("mobAttack")[0]).getAsString().replaceAll("\"", "").equalsIgnoreCase(EntityList.getEntityString(event.source.getEntity()))) {
					GenericEffects.rand.setSeed(event.entityLiving.worldObj.getTotalWorldTime());
					if (GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive) disease.getParameters("mobAttack")[1]).getAsInt()) {
						DiseaseHelper.addDisease(event.entityLiving, disease);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void mobAroundEvent(LivingEvent.LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			for (Disease disease : Diseases.diseases) {
				if (EntityList.classToStringMapping != null && disease.getWaysToContract() != null && disease.getParameters("mob") != null && disease.getWaysToContract().contains("mob")) {
					int distance = ((JsonPrimitive) disease.getParameters("mob")[1]).getAsInt();
					for (Entity entity : (List<Entity>) event.entityLiving.worldObj.loadedEntityList) {
						if (entity instanceof EntityPlayer) {
							continue;
						}
						if (EntityList.getEntityString(entity).equalsIgnoreCase(((JsonPrimitive) disease.getParameters("mob")[0]).getAsString()) && entity.getDistanceToEntity(event.entityLiving) <= distance && GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive) disease.getParameters("mob")[2]).getAsInt()) {
							DiseaseHelper.addDisease(event.entityLiving, disease);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@SubscribeEvent
	public void biomeContraction(LivingEvent.LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			if (event.entityLiving.worldObj.getTotalWorldTime() % 20 == 0) {
				for (Disease disease : Diseases.diseases) {
					if (disease.getWaysToContract() != null && disease.getParameters("temp") != null && disease.getWaysToContract().contains("temp")) {
						float temperature = ((JsonPrimitive) disease.getParameters("temp")[0]).getAsFloat();
						int randomNumber = ((JsonPrimitive) disease.getParameters("temp")[1]).getAsInt();
						boolean warmDetection = ((JsonPrimitive) disease.getParameters("temp")[2]).getAsBoolean();
						float temp = event.entityLiving.worldObj.getBiomeGenForCoordsBody(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posZ)).getFloatTemperature(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posY), MathHelper.floor_double(event.entityLiving.posZ));

						if (((warmDetection && temp >= temperature) || (!warmDetection && temp <= temperature)) && GenericEffects.rand.nextInt(1000000) >= randomNumber) {
							DiseaseHelper.addDisease(event.entityLiving, disease);
						}
					}

					// Stat Contraction

					if (disease.getWaysToContract() != null && disease.getParameters("stat") != null && disease.getWaysToContract().contains("stat")) {
						String statName = ((JsonPrimitive) disease.getParameters("stat")[0]).getAsString();
						int statValue = ((JsonPrimitive) disease.getParameters("stat")[1]).getAsInt();
						boolean useBooleanFlag = ((JsonPrimitive) disease.getParameters("stat")[2]).getAsBoolean();
						int randomNumber = ((JsonPrimitive) disease.getParameters("stat")[3]).getAsInt();
						if (event.entityLiving instanceof EntityPlayerMP) {
							EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
							if (player.func_147099_x().writeStat(StatHelper.getStatBaseFromName(statName)) >= statValue) {
								if (GenericEffects.rand.nextInt(1000000) > randomNumber) {
									if (useBooleanFlag) {
										if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10) || !player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean("has" + disease.getId())) {
											if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10)) {
												player.getEntityData().setTag(player.PERSISTED_NBT_TAG, new NBTTagCompound());
											}
											player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setBoolean("has" + disease.getId(), true);
										}
									}

									DiseaseHelper.addDisease(event.entityLiving, disease);
								}
							}
						}
					}

				}

			}
		}
	}

	@SubscribeEvent
	public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (DiseaseHelper.isDiseaseActive(event.entityLiving,disease)) {
				if (event.entityLiving.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name","")) && event.entityLiving.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) == 1 && !event.entityLiving.isDead) {
					MinecraftForge.EVENT_BUS.post(new DiseaseEvent.DiseaseEndEvent(disease,event.entityLiving));
				}
				GenericEffects.applyEffects(event.entityLiving,disease);
				int newDiseaseTimer = event.entityLiving.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) - 1;
				event.entityLiving.getEntityData().setInteger(disease.getUnlocalizedName().replaceAll(".name",""),newDiseaseTimer);
			}
		}
		if (event.entityLiving.worldObj.getTotalWorldTime() % 60 == 0) {
			if (!event.entityLiving.worldObj.isRemote) {
				if (event.entityLiving instanceof EntityPlayer) {
					EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
					PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG, player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)), player);
				}
			}
		}
	}

	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent event) {
		if (VersionJSON.versions != null) {
			double biggestDouble = 2.0;
			for (DCVersion version : VersionJSON.versions) {
				if (version.mcVersion == Double.parseDouble(MinecraftForge.MC_VERSION.substring(MinecraftForge.MC_VERSION.indexOf(".") + 1))) {
					if (version.versionNumber > biggestDouble) {
						biggestDouble = version.versionNumber;
					}
				}
			}
			event.player.addChatComponentMessage(new ChatComponentTranslation("disease.update.new", biggestDouble));
			event.player.addChatComponentMessage(new ChatComponentText(VersionJSON.getVersion(biggestDouble) != null ? VersionJSON.getVersion(biggestDouble).updateString : "[ERROR: Changelog not available]"));
		}
	}

	@SubscribeEvent
	public void onEaten(PlayerUseItemEvent.Finish event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			if (event.item != null) {
				for (Disease disease : Diseases.diseases) {
					if (disease.getWaysToContract() != null && disease.getWaysToContract().contains("eaten")) {
						if (disease.getParameters("eaten") != null) {
							//event.item.getItem() == Item.getItemById(itemID) &&
							int itemID = -1;
							if (isInteger(((JsonPrimitive)disease.getParameters("eaten")[0]).getAsString())) {
								itemID = ((JsonPrimitive) disease.getParameters("eaten")[0]).getAsInt();
							}
							int itemDamage = ((JsonPrimitive) disease.getParameters("eaten")[1]).getAsInt();
							int randomChance = ((JsonPrimitive) disease.getParameters("eaten")[2]).getAsInt();
							if ((itemID != -1 && event.item.getItem() == Item.getItemById(itemID)) || (GameData.getItemRegistry().getObject(((JsonPrimitive) disease.getParameters("eaten")[0]).getAsString().replaceAll("\"", "")) != null && event.item.getItem() == GameData.getItemRegistry().getObject(((String) disease.getParameters("eaten")[0]).replaceAll("\"", "")))) {
								if (event.item.getItemDamage() == itemDamage && GenericEffects.rand.nextInt(1000000) > randomChance) {
									DiseaseHelper.addDisease(event.entityLiving, disease);
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void endDiseases(DiseaseEvent.DiseaseEndEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10)) {
				player.getEntityData().setTag(player.PERSISTED_NBT_TAG, new NBTTagCompound());
			}
			player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setBoolean(event.disease.getUnlocalizedName().replaceAll(".name", ".complete"), true);
			System.out.println("Goodbye Caroline!");
			System.out.println(event.entityLiving + " " + player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean(event.disease.getUnlocalizedName().replaceAll(".name", ".complete")));
			if (!player.worldObj.isRemote) {
				PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG,player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)),(EntityPlayerMP)player.worldObj.getClosestPlayer(player.posX,player.posY,player.posZ,10));
			}
		}
	}

	boolean isInteger(String intString) {
		try {
			Integer.parseInt(intString);
		} catch(Exception ex) {
			return false;
		}
		return true;
	}
}
