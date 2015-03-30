package mc.Mitchellbrine.diseaseCraft.config;

import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ConfigRegistry {

	public static Set<DCModule> enabledMods;

	public static int STATE = 0;

	public static void init(File configDirectory) {
		enabledMods = new HashSet<DCModule>();
		for (DCModule module : ClassHelper.modules.values()) {
			enabledMods.add(module);
		}
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
			switch (STATE) {
				case 0:
					ClassHelper.interfaces.get(module).preInit();
					break;
				case 1:
					ClassHelper.interfaces.get(module).init();
					break;
				case 2:
					ClassHelper.interfaces.get(module).postInit();
					break;
				case 3:
					ClassHelper.interfaces.get(module).serverStart();
					break;
			}
		}
		STATE++;
	}

}
