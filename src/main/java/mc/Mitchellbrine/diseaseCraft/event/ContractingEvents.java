package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.JsonPrimitive;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import mc.Mitchellbrine.diseaseCraft.utils.StatHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

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
					if (EntityList.getEntityString(entity).equalsIgnoreCase(((JsonPrimitive)disease.getParameters("mob")[0]).getAsString()) && entity.getDistanceToEntity(event.entityLiving) <= distance && GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive)disease.getParameters("mob")[2]).getAsInt()) {
						DiseaseHelper.addDisease(event.entityLiving,disease);
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
				if (disease.getWaysToContract() != null && disease.getParameters("biome") != null && disease.getWaysToContract().contains("biome")) {
					float temperature = ((JsonPrimitive)disease.getParameters("biome")[0]).getAsFloat();
					int randomNumber = ((JsonPrimitive)disease.getParameters("biome")[1]).getAsInt();
					boolean warmDetection = ((JsonPrimitive)disease.getParameters("biome")[2]).getAsBoolean();
					String biome = "missingno";
					if (disease.getParameters("biome").length > 3) {
						biome = ((JsonPrimitive)disease.getParameters("biome")[3]).getAsString();
					}
					if (!biome.equalsIgnoreCase("missingno")) {
						if (event.entityLiving.worldObj.getBiomeGenForCoordsBody(event.entityLiving.getPosition()).biomeName.equalsIgnoreCase(biome) && GenericEffects.rand.nextInt(1000000) >= randomNumber) {
							DiseaseHelper.addDisease(event.entityLiving,disease);
						}
					} else {
						float temp = event.entityLiving.worldObj.getBiomeGenForCoordsBody(event.entityLiving.getPosition()).getFloatTemperature(event.entityLiving.getPosition());
						if (((warmDetection && temp >= temperature) || (!warmDetection && temp <= temperature)) && GenericEffects.rand.nextInt(1000000) >= randomNumber) {
							DiseaseHelper.addDisease(event.entityLiving,disease);
						}
					}
				}

				// Stat Contraction

				if (disease.getWaysToContract() != null && disease.getParameters("stat") != null && disease.getWaysToContract().contains("stat")) {
					String statName = ((JsonPrimitive)disease.getParameters("stat")[0]).getAsString();
					int statValue = ((JsonPrimitive)disease.getParameters("stat")[1]).getAsInt();
					boolean useBooleanFlag = ((JsonPrimitive)disease.getParameters("stat")[2]).getAsBoolean();
					if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
						if (player.getStatFile().readStat(StatHelper.getStatBaseFromName(statName)) >= statValue) {
							if (useBooleanFlag) {
								if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG,10) || !player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean("has"+disease.getId())) {
									DiseaseHelper.addDisease(event.entityLiving,disease);
									if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG,10)) {
										player.getEntityData().setTag(player.PERSISTED_NBT_TAG,new NBTTagCompound());
									}
									player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setBoolean("has"+disease.getId(),true);
								}
							} else {
								DiseaseHelper.addDisease(event.entityLiving,disease);
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
			}
		}
	}


}
