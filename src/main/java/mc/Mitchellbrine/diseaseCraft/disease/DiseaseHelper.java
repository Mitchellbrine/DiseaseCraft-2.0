package mc.Mitchellbrine.diseaseCraft.disease;

import mc.Mitchellbrine.diseaseCraft.api.Disease;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseHelper {

	public static void addDisease(EntityLivingBase entity, Disease disease) {
		addDisease(entity,disease, 1200);
	}

	public static void addDisease(EntityLivingBase entity, Disease disease, int ticks) {
		if (!entity.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name", "")) || entity.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name", "")) == 0) {
			entity.getEntityData().setInteger(disease.getUnlocalizedName().replaceAll(".name", ""), ticks);
		}
	}

	public static boolean isDiseaseActive(EntityLivingBase entity,Disease disease) {
		return entity.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name","")) && entity.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) > 0;
	}

	public static void removeDisease(EntityLivingBase entity, Disease disease) {
		if (entity.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name",""))) {
			entity.getEntityData().setInteger(disease.getUnlocalizedName().replaceAll(".name",""),0);
		}
	}

	public static boolean diseaseExists(String id) {
		for (Disease disease : Diseases.diseases) {
			if (disease.getId().equalsIgnoreCase(id))
				return true;
		}
		return false;
	}

	public static Disease getDiseaseInstance(String id) {
		for (Disease disease : Diseases.diseases) {
			if (disease.getId().equalsIgnoreCase(id))
				return disease;
		}
		return null;
	}

	public static List<Disease> getActiveDiseases(EntityLivingBase entity) {
		List<Disease> actives = new ArrayList<Disease>();
		for (Disease disease : Diseases.diseases) {
			if (isDiseaseActive(entity,disease))
				actives.add(disease);
		}
		return actives;
	}

}
