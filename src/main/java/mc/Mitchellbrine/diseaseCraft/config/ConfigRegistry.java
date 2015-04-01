package mc.Mitchellbrine.diseaseCraft.config;

import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;

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
