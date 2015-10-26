package mc.Mitchellbrine.diseaseCraft.modules.med.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.FMLInjectionData;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseJSON;
import mc.Mitchellbrine.diseaseCraft.modules.med.recipe.MedicationRecipes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MedUtils {

	public static DamageSource medication = new DamageSource("medication").setDamageBypassesArmor();
	private static List<String> medConfigs;

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


	public void painKiller(LivingAttackEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			System.out.println("Hurt event called!");
			if (event.ammount > event.entityLiving.getHealth()) {
				event.setCanceled(false);
			} else {
				event.entityLiving.setHealth(event.entityLiving.getHealth() - event.ammount);
				event.setCanceled(true);
			}
		}
	}

	public static void findAllMeds() {
		File folder = new File((FMLInjectionData.data()[6]) + File.separator + "DiseaseCraft" + File.separator + "Medication");
		medConfigs = new ArrayList<String>();
		if (!folder.exists()) {
			folder.mkdirs();
		}
		if (folder.listFiles() != null) {
			for (File file : folder.listFiles()) {
				if (file != null && !file.isDirectory() && file.getName().endsWith(".json")) {
					medConfigs.add(file.getPath());
				}
			}
		}
	}

	public static void readAllMeds() {
		for (String file : medConfigs) {
			readMeds(file);
		}
	}

	private static void readMeds(String fileName) {
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Medication.class, new MedJSON());
			Gson gson = builder.create();

			File file = new File(fileName);

			Medication[] diseases = gson.fromJson(new FileReader(file), Medication[].class);

			for (Medication disease : diseases) {
				if (disease != null) {
					MedicationRecipes.addMedicationType(new ItemStack(GameData.getItemRegistry().getObject(disease.itemName),1,disease.itemMeta),disease.diseaseHeal,disease.medName,disease.suppresents);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
