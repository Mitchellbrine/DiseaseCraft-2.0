package mc.Mitchellbrine.diseaseCraft.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;
import net.minecraftforge.common.config.Configuration;

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

	public static void init(File configDirectory) {
		enabledMods = ClassHelper.modules.values();
		try {
			File moduleConfig = new File(configDirectory,"DC-modules.cfg");
			if (moduleConfig.exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(moduleConfig)));
				String s;

				while ((s = reader.readLine()) != null) {
					if (!Boolean.parseBoolean(s.substring(s.indexOf(":")))) {
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
			LanguageRegistry.instance().loadLocalization(file.toURI().toURL(),"en_US",false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public static void doConfig(Configuration config) {

		config.load();

		autoUpdate = config.get(Configuration.CATEGORY_GENERAL,"autoUpdate",true,"Auto-download disease patches").getBoolean(true);

		config.save();

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
