package mc.Mitchellbrine.diseaseCraft.disease;

import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
public class Diseases {

	public static List<Disease> diseases;
	public static List<Integer> acceptableModes;
	public static Map<Integer,Method> modesAndMethods;
	public static Map<Method,String> modesAndNames;

	static {
		diseases = new ArrayList<Disease>();
		acceptableModes = new ArrayList<Integer>();
		modesAndMethods = new HashMap<Integer, Method>();
		modesAndNames = new HashMap<Method, String>();
		addMode(-1, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "jitter");
		addMode(-2, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "dropItem");
		addMode(-3, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "hydrophobia");
		addMode(-4, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "death");
		acceptableModes.add(-5 /* *RESERVED* */);
		acceptableModes.add(-6 /* *RESERVED* */);
	}

	@SuppressWarnings("unchecked")
	public static void registerDisease(Disease disease) {
		if (disease.isRequirementMet()) {
			ArrayList<Integer> effects = (ArrayList<Integer>)disease.effects;
			ArrayList<Integer> correctEffects = new ArrayList<Integer>();

			for (int effect : effects) {
				if (acceptableModes.contains(effect) || Potion.potionTypes[effect] != null)
				correctEffects.add(effect);
			}

			disease.effects = correctEffects;

			diseases.add(disease);
		}
	}

	public static void addMode(int modeNumber, String className, String methodName) {
		try {
			addMode(modeNumber, Class.forName(className).getMethod(methodName,EntityLivingBase.class,Disease.class));
		} catch (Exception ex) {
			DiseaseCraft.logger.error("Caught an error while adding mode with the number " + modeNumber,ex);
		}
	}

	public static void addMode(int modeNumber, Method method) {
		if (acceptableModes.contains(modeNumber)) {
			DiseaseCraft.logger.error("The id " + modeNumber + " is already taken for modes. Please try another and report this to the mod author.");
		}
		acceptableModes.add(modeNumber);
		modesAndMethods.put(modeNumber, method);
		if (method.getName().lastIndexOf(".") != -1) {
			modesAndNames.put(method, method.getName().substring(method.getName().lastIndexOf(".")));
		} else {
			modesAndNames.put(method, method.getName());
		}
	}

	public static String getDiseaseName(String id) {
		for (Disease disease : diseases) {
			if (disease.getId().equalsIgnoreCase(id))
				return disease.getUnlocalizedName();
		}
		return null;
	}

}
