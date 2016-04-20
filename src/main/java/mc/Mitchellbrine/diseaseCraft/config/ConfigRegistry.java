package mc.Mitchellbrine.diseaseCraft.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ConfigRegistry {

	public static Collection<DCModule> enabledMods;

	public static int STATE = 0;
	public static boolean autoUpdate;
	public static String[] userDiseases;
	public static int journalLevel;
	public static int diseaseProgression;
	public static boolean useNativeDiseases;
	public static int diseasedPlantsChance;
	public static boolean doDiseasedCrops;
	public static int diseasedCropsRadius;

	public static boolean useTempCompat;
	public static String tempTag;
	public static float baseTemp;

	public static Logger logger = LogManager.getLogger("DiseaseCraft-Config");

	@SuppressWarnings("deprecation")
	public static void init(File configDirectory) {
		enabledMods = ClassHelper.modules.values();
		try {
			File moduleConfig = new File(configDirectory,"DC-modules.cfg");
			if (moduleConfig.exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(moduleConfig)));
				String s;

				while ((s = reader.readLine()) != null) {
					if (!Boolean.parseBoolean(s.substring(s.indexOf(":") + 1))) {
						for (DCModule module : enabledMods) {
							if (module.id().equalsIgnoreCase(s.substring(0,s.indexOf(":")))) {
								enabledMods.remove(module);
								break;
							}
						}
					}
				}

				reader.close();
			} else {
				moduleConfig.createNewFile();
				PrintWriter writer = new PrintWriter(moduleConfig);
				for (DCModule module : ClassHelper.modules.values()) {
					writer.println(module.id() + ":" + module.isEnabled());
				}
				writer.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			try {
				/*List<IResourcePack> defaultResourcePacks = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao", "ap");
				File file = new File(configDirectory, "diseaseTranslate.lang");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileResourcePack langPack = new FileResourcePack(file);
				defaultResourcePacks.add(langPack);
				} */

			File file = new File(configDirectory, "diseaseTranslate.lang");
				if (!file.exists()) {
					file.createNewFile();
				}
			LanguageRegistry.instance().loadLocalization(file.toURI().toURL(),"en_US",false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public static void doConfig(Configuration config) {

		config.load();

		autoUpdate = config.get(Configuration.CATEGORY_GENERAL,"autoUpdate",false,"Auto-download disease patches\n\nIf you want to get the latest version of the diseases, download them at https://github.com/Mitchellbrine/DiseaseCraft-2.0/blob/master/diseaseJSON/").getBoolean(false);
		userDiseases = config.getStringList("userDiseases", Configuration.CATEGORY_GENERAL,new String[]{},"The diseases that go in the user medical journal");
		journalLevel = config.getInt("journalLevel",Configuration.CATEGORY_GENERAL,0,0,1,"Specify which diseases appear in journals\n\n0 = Regular Diseases\n1 = Joke Diseases\n\n");
		diseaseProgression = config.getInt("diseaseProgression",Configuration.CATEGORY_GENERAL,0,0,1,"Should journals hide diseases until player retrieves them?");
		useNativeDiseases = config.getBoolean("useNativeJSON",Configuration.CATEGORY_GENERAL,true,"Loads the JSON file located within the jar of the mod");
		diseasedPlantsChance = config.getInt("diseasedPlantsChance",Configuration.CATEGORY_GENERAL,2,0,100,"The chance that a plant will become diseased (calculated every 2 seconds when within 5x5x5)");
		doDiseasedCrops = config.getBoolean("doDiseasedCrops",Configuration.CATEGORY_GENERAL,true,"Should diseased crops be calculated on movement? (Increases performance) (Does not remove crops functionality)");
		diseasedCropsRadius = config.getInt("diseasedCropsRadius","diseasedCrops",5,1,6,"Changes how many blocks are calculated for diseased crops (lower values increase server tick rate)");

		useTempCompat = config.getBoolean("useTempCompat","compat",false,"Use another mod to calculate temperature or to store temperature. This removes cross mod incompatibilities");
		tempTag = config.getString("temperatureTag","compat","","Sets which temperature tag is used for calculating temperature (for example \"bodyTemp\" from Enviromine");
		baseTemp = config.getFloat("baseTemperature","compat",1.0f,Float.MIN_VALUE,Float.MAX_VALUE,"Sets which double to multiply by to accurately account for the diseases in-game (for example 37 degrees Celsius is a regular body temperature and could be used");
		config.save();

		System.out.println(useNativeDiseases);

	}

	public static void triggerState() {
		for (DCModule module : enabledMods) {
			ClassHelper.invokeMethod(STATE,ClassHelper.moduleMap.get(module));
		}
		STATE++;
	}

	public static boolean isModuleLoaded(String name) {
		for (DCModule module : enabledMods) {
			if (module.id().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

}
