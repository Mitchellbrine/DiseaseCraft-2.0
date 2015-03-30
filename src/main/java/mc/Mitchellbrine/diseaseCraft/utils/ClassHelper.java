package mc.Mitchellbrine.diseaseCraft.utils;

import cpw.mods.fml.common.discovery.ASMDataTable;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.IModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ClassHelper {

	public static Map<String,DCModule> modules;
	public static Map<DCModule,IModule> interfaces;

	public static Logger logger = LogManager.getLogger("DC-Module");

	public static void searchForModules(ASMDataTable asmData) {

		Set<Class<?>> modClasses = new HashSet<Class<?>>();

		for (ASMDataTable.ASMData data : asmData.getAll(DCModule.class.getName())) {
			try {
				if (Class.forName(data.getClassName()) != null) {
					modClasses.add(Class.forName(data.getClassName()));
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}

		modules = new HashMap<String, DCModule>();
		interfaces = new HashMap<DCModule, IModule>();

		for (Class<?> clazz : modClasses) {
			if (clazz.getAnnotation(DCModule.class) != null && IModule.class.isAssignableFrom(clazz)) {
					DCModule module = clazz.getAnnotation(DCModule.class);
					modules.put(module.id(),module);
					try {
						interfaces.put(module, clazz.asSubclass(IModule.class).newInstance());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
		}

		for (DCModule module : modules.values()) {
			logger.info("Loaded module " + module.id() + " from " + module.modid() + " for DC version " + module.dcVersion());
		}

	}

}
