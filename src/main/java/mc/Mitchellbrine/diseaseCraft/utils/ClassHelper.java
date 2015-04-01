package mc.Mitchellbrine.diseaseCraft.utils;

import cpw.mods.fml.common.discovery.ASMDataTable;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import mc.Mitchellbrine.diseaseCraft.modules.ModuleWarfare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ClassHelper {

	public static Map<String,DCModule> modules;
	public static Map<DCModule,Module> moduleMap;

	public static Logger logger = LogManager.getLogger("DC-Module");

	public static ClassHelper INSTANCE;

	public ClassHelper() {
		INSTANCE = this;
	}

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
		moduleMap = new HashMap<DCModule, Module>();

		for (Class<?> clazz : modClasses) {
			if (clazz.getAnnotation(DCModule.class) != null && Module.class.isAssignableFrom(clazz)) {
					DCModule module = clazz.getAnnotation(DCModule.class);
					modules.put(module.id(),module);
					try {
						moduleMap.put(module, ((Class<? extends Module>) clazz).newInstance());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
		}

		for (DCModule module : modules.values()) {
			logger.info("Loaded module " + module.id() + " from " + module.modid() + " for DC version " + module.dcVersion());
		}

	}

	@SuppressWarnings("unchecked")
	public static void invokeMethod(int identifier, Module className) {
		try {
			switch (identifier) {
				case 0:
					className.preInit();
					break;
				case 1:
					className.init();
					break;
				case 2:
					className.postInit();
					break;
				case 3:
					className.serverStart();
					break;
			}
		} catch (Throwable ex) {
			logger.fatal("Exception raised with invoking " + identifier + " from class " + className,ex);
		}
	}

}
