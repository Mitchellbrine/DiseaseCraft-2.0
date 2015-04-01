package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.JsonPrimitive;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.dio.DCVersion;
import mc.Mitchellbrine.diseaseCraft.dio.VersionJSON;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import mc.Mitchellbrine.diseaseCraft.entity.EntityRat;
import mc.Mitchellbrine.diseaseCraft.utils.StatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ContractingEvents {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (event.source != null && event.source.getEntity() != null && EntityList.classToStringMapping != null && disease.getWaysToContract() != null && EntityList.getEntityString(event.source.getEntity()) != null && disease.getParameters("mobAttack") != null && disease.getWaysToContract().contains("mobAttack") && ((JsonPrimitive)disease.getParameters("mobAttack")[0]).getAsString().replaceAll("\"", "").equalsIgnoreCase(EntityList.getEntityString(event.source.getEntity()))) {
				GenericEffects.rand.setSeed(event.entityLiving.worldObj.getTotalWorldTime());
				if (GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive)disease.getParameters("mobAttack")[1]).getAsInt()) {
					DiseaseHelper.addDisease(event.entityLiving,disease);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void mobAroundEvent(LivingEvent.LivingUpdateEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (EntityList.classToStringMapping != null && disease.getWaysToContract() != null && disease.getParameters("mob") != null && disease.getWaysToContract().contains("mob")) {
				int distance = ((JsonPrimitive)disease.getParameters("mob")[1]).getAsInt();
				for (Entity entity : (List<Entity>)event.entityLiving.worldObj.loadedEntityList) {
					if (entity instanceof EntityPlayer) {
						continue;
					}
					if (disease.getParameters("mob")[0] == null) {
						System.out.println("First parameter is null");
					}
					if (disease.getParameters("mob")[1] == null) {
						System.out.println("Second parameter is null");
					}
					if (disease.getParameters("mob")[2] == null) {
						System.out.println("Second parameter is null");
					}
					if (GenericEffects.rand == null) {
						GenericEffects.rand = new Random();
					}
					if (EntityList.classToStringMapping == null) {
						System.out.println("The map is null!");
					}
					if (EntityList.classToStringMapping.get(entity.getClass()) == null) {
						System.out.println("EntityString is null");
						continue;
					}
					if (EntityList.getEntityString(entity).equalsIgnoreCase(((JsonPrimitive)disease.getParameters("mob")[0]).getAsString()) && entity.getDistanceToEntity(event.entityLiving) <= distance && GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive)disease.getParameters("mob")[2]).getAsInt()) {
						DiseaseHelper.addDisease(event.entityLiving, disease);
					} else if (EntityList.getEntityString(entity) == null) {
						System.out.println("EntityString is null");
					} else if (disease.getParameters("mob") == null) {
						System.out.println("Parameters are null");
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@SubscribeEvent
	public void biomeContraction(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.getTotalWorldTime() % 20 == 0) {
			for (Disease disease : Diseases.diseases) {
				if (disease.getWaysToContract() != null && disease.getParameters("temp") != null && disease.getWaysToContract().contains("temp")) {
					float temperature = ((JsonPrimitive)disease.getParameters("temp")[0]).getAsFloat();
					int randomNumber = ((JsonPrimitive)disease.getParameters("temp")[1]).getAsInt();
					boolean warmDetection = ((JsonPrimitive)disease.getParameters("temp")[2]).getAsBoolean();
					float temp = event.entityLiving.worldObj.getBiomeGenForCoordsBody(MathHelper.floor_double(event.entityLiving.posX),MathHelper.floor_double(event.entityLiving.posZ)).getFloatTemperature(MathHelper.floor_double(event.entityLiving.posX),MathHelper.floor_double(event.entityLiving.posY),MathHelper.floor_double(event.entityLiving.posZ));

					if (((warmDetection && temp >= temperature) || (!warmDetection && temp <= temperature)) && GenericEffects.rand.nextInt(1000000) >= randomNumber) {
						DiseaseHelper.addDisease(event.entityLiving,disease);
					}
				}

				// Stat Contraction

				if (disease.getWaysToContract() != null && disease.getParameters("stat") != null && disease.getWaysToContract().contains("stat")) {
					String statName = ((JsonPrimitive)disease.getParameters("stat")[0]).getAsString();
					int statValue = ((JsonPrimitive)disease.getParameters("stat")[1]).getAsInt();
					boolean useBooleanFlag = ((JsonPrimitive)disease.getParameters("stat")[2]).getAsBoolean();
					int randomNumber = ((JsonPrimitive)disease.getParameters("stat")[3]).getAsInt();
					if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
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

	@SubscribeEvent
	public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (DiseaseHelper.isDiseaseActive(event.entityLiving,disease)) {
				GenericEffects.applyEffects(event.entityLiving,disease);
				int newDiseaseTimer = event.entityLiving.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) - 1;
				event.entityLiving.getEntityData().setInteger(disease.getUnlocalizedName().replaceAll(".name",""),newDiseaseTimer);
			}
		}
	}

	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent event) {
		if (VersionJSON.versions != null) {
			double biggestDouble = 2.0;
			for (DCVersion version : VersionJSON.versions) {
				if (version.mcVersion == Double.parseDouble(MinecraftForge.MC_VERSION.substring(MinecraftForge.MC_VERSION.indexOf(".")))) {
					System.out.println(Double.parseDouble(MinecraftForge.MC_VERSION.substring(MinecraftForge.MC_VERSION.indexOf("."))));
					if (version.versionNumber > biggestDouble) {
						biggestDouble = version.versionNumber;
					}
				}
			}
			event.player.addChatComponentMessage(new ChatComponentTranslation("disease.update.new", biggestDouble));
			event.player.addChatComponentMessage(new ChatComponentText(VersionJSON.getVersion(biggestDouble).updateString));
		}
	}


}
