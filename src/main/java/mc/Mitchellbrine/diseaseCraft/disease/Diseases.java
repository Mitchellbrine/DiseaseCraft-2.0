package mc.Mitchellbrine.diseaseCraft.disease;

import com.google.gson.JsonPrimitive;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

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

	private static List<String> types;

	public static Map<String, List<Disease>> diseaseTypes;

	public static Map<World, List<EntityLivingBase>> diseasedEntities;

	public static List<Class<? extends EntityLivingBase>> entityClasses;
	public static List<Class<? extends EntityLivingBase>> mobClasses;
	public static List<Class<? extends EntityLivingBase>> mobAttackClasses;

	static {
		diseases = new ArrayList<Disease>();
		acceptableModes = new ArrayList<Integer>();
		modesAndMethods = new HashMap<Integer, Method>();
		modesAndNames = new HashMap<Method, String>();
		types = new ArrayList<String>();
		diseaseTypes = new HashMap<String, List<Disease>>();
		diseasedEntities = new HashMap<World, List<EntityLivingBase>>();
		entityClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		addMode(-1, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "jitter");
		addMode(-2, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "dropItem");
		addMode(-3, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "hydrophobia");
		addMode(-4, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "death");
		addMode(-5, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "coughing");
		addMode(-6, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "sneezing");
		addMode(-7, "mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects", "brainReanimation");

		types.add("eaten");
		types.add("block-contact");
		types.add("temp");
		types.add("mob");
		types.add("mobAttack");
		types.add("crops");

		MinecraftForge.EVENT_BUS.register(new BloodTypeHelper());
		FMLCommonHandler.instance().bus().register(new BloodTypeHelper());

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

			for (String type : types) {
				if (disease.getParameters(type) != null) {
					List<Disease> diseases1;
					if (diseaseTypes.containsKey(type)) {
						diseases1 = diseaseTypes.get(type);
					} else {
						diseases1 = new ArrayList<Disease>();
					}
					if (!diseases1.contains(disease)) {
						diseases1.add(disease);
					}
					diseaseTypes.put(type,diseases1);
					DiseaseCraft.logger.info("Registered " + disease.getId() + " to the type " + type);
					if (type.equalsIgnoreCase("mob") || type.equalsIgnoreCase("mobAttack")) {
						if (type.equalsIgnoreCase("mobAttack")) {
							for (int i = 1; i < disease.getParameters(type).length;i++) {
								if (EntityList.classToStringMapping.containsValue(disease.getParameters(type)[i])) {
									Class<? extends EntityLivingBase> clazz = (Class<? extends EntityLivingBase>)EntityList.stringToClassMapping.get(((JsonPrimitive)disease.getParameters(type)[i]).getAsString());
									if (!entityClasses.contains(clazz))
										entityClasses.add(clazz);
									if (!mobAttackClasses.contains(clazz))
										mobAttackClasses.add(clazz);
								}
							}
						} else {
							for (int i = 2; i < disease.getParameters(type).length;i++) {
								if (EntityList.classToStringMapping.containsValue(disease.getParameters(type)[i])) {
									Class<? extends EntityLivingBase> clazz = (Class<? extends EntityLivingBase>)EntityList.stringToClassMapping.get(((JsonPrimitive)disease.getParameters(type)[i]).getAsString());
									if (!entityClasses.contains(clazz))
										entityClasses.add(clazz);
									if (!mobClasses.contains(clazz))
										mobClasses.add(clazz);
								}
							}
						}
					}
				}
			}
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
			//System.out.println("Disease: " + disease.getId());
			//System.out.println("Expected: " + disease.getId() + " | Received: " + id);
			if (disease.getId().equalsIgnoreCase(id)) {
				return disease.getUnlocalizedName();
			} else {
				continue;
			}
		}
		return null;
	}

}
