package mc.Mitchellbrine.diseaseCraft.modules.med.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.modules.med.recipe.MedicationRecipes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MedUtils {

	public static DamageSource medication = new DamageSource("medication").setDamageBypassesArmor();

	public static boolean areMedsActive(EntityLivingBase entity, String id) {
		return entity.getEntityData().hasKey("block" + id) && entity.getEntityData().getInteger("block" + id) > 0;
	}

	public static void applyEffect(EntityLivingBase entity, NBTTagCompound nbt) {
		if (!nbt.hasKey("diseaseHeal")) {
			return;
		}

		if (!areMedsActive(entity,nbt.getString("diseaseHeal"))) {
			entity.getEntityData().setInteger("block" + nbt.getString("diseaseHeal"), 6000);
		} else {
			int getCurrent = entity.getEntityData().getInteger("block"+nbt.getString("diseaseHeal"));
			entity.getEntityData().setInteger("block"+nbt.getString("diseaseHeal"),getCurrent + 6000);
		}

		if (nbt.hasKey("dangerous") && nbt.getBoolean("dangerous")) {
			entity.getEntityData().setInteger("block"+nbt.getString("diseaseHeal"),1000000);
		}

		if (nbt.hasKey("tooSuppressed") && nbt.getBoolean("tooSuppressed")) {
			entity.getEntityData().setInteger("block"+nbt.getString("diseaseHeal"),0);
		}

	}

	@SubscribeEvent
	public void diseasePrevention(DiseaseEvent.DiseaseEffectEvent event) {
		if (areMedsActive(event.entityLiving,event.disease.getId())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void medTimedown(LivingEvent.LivingUpdateEvent event) {
		for (String diseaseId : MedicationRecipes.diseaseRemoval.values()) {
			if (areMedsActive(event.entityLiving,diseaseId)) {
				int newMeds = event.entityLiving.getEntityData().getInteger("block"+diseaseId) - 1;
				event.entityLiving.getEntityData().setInteger("block"+diseaseId,newMeds);
				if (newMeds > 24000) {
					event.entityLiving.attackEntityFrom(medication,1.0F);
				}
			}
		}
	}

}
