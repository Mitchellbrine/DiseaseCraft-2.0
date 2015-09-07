package mc.Mitchellbrine.diseaseCraft.utils;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.discovery.ASMDataTable;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
					if (getModule(module.id()) != null) {
						logger.error("Module with the id \""+module.id()+"\" already exists, skipping module from package \"" + clazz.getPackage().getName() + "\"");
						continue;
					}
					modules.put(module.id(),module);
					try {
						moduleMap.put(module, ((Class<? extends Module>) clazz).newInstance());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
		}

		List<DCModule> requirementsNotMet = new ArrayList<DCModule>();

		for (DCModule module : modules.values()) {
			if (module.requiredModules().length == 0)
				continue;
			for (String require : module.requiredModules()) {
				if (getModule(require) == null) {
					requirementsNotMet.add(module);
					break;
				}
		}
		}

		for (DCModule module : requirementsNotMet) {
			modules.remove(module.id());
			moduleMap.remove(module);
		}

		Map<String, DCModule> sortedModules = new HashMap<String, DCModule>(modules);
		Map<DCModule, Module> sortedModuleMap = new HashMap<DCModule, Module>(moduleMap);

		Map<DCModule, Module> addLater = new HashMap<DCModule, Module>();
		List<DCModule> removeLater = new ArrayList<DCModule>();

		for (DCModule module : sortedModules.values()) {
			if (module.requiredModules().length == 0)
				continue;
			for (String id : module.requiredModules()) {
				if (getModuleFromList(sortedModules.values(),id) == null) {
					removeLater.add(module);
					addLater.put(module,sortedModuleMap.get(module));
				}
			}
		}

		for (DCModule module : removeLater) {
			sortedModules.remove(module.id());
			sortedModuleMap.remove(module);

			sortedModules.put(module.id(),module);
			sortedModuleMap.put(module,addLater.get(module));
		}

		addLater.clear();
		removeLater.clear();

		List<ModContainer> fmlMods = getPrivateObject(Loader.instance(), "mods");
		List<ModContainer> newMods = new ArrayList<ModContainer>();
		newMods.addAll(fmlMods);
		for (DCModule module : modules.values()) {
			ModMetadata fakeMeta = new ModMetadata();
			fakeMeta.modId = "DCMODULE" + module.id();
			fakeMeta.name = "[DC] " + module.id().substring(0,1).toUpperCase() + module.id().substring(1);
			fakeMeta.version = module.version();
			fakeMeta.description = module.description().isEmpty() ? "A module for DiseaseCraft" : module.description();
			fakeMeta.parent = "DiseaseCraft";
			newMods.add(new DummyMod(fakeMeta));
		}
		setPrivateObject(Loader.instance(), newMods, "mods");


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

	public static DCModule getModule(String id) {
		for (String module : modules.keySet()) {
			if (module.equals(id))
				return modules.get(module);
		}
		return null;
	}

	public static DCModule getModuleFromList(Collection<DCModule> modules, String id) {
		for (DCModule module : modules) {
			if (module.id().equals(id))
				return module;
		}
		return null;
	}

	public static <T> T getPrivateObject(Object object, String... names) {
		Class<?> cls = object.getClass();
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return (T) field.get(object);
			} catch (Exception ex) {

			}
		}

		return null;
	}

	public static boolean setPrivateObject(Object object, Object value, String... names) {
		Class<?> cls = object.getClass();
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				field.set(object, value);
				return true;
			} catch (Exception ex) {

			}
		}

		return false;
	}

	private static class DummyMod extends DummyModContainer {
		public DummyMod(ModMetadata meta) {
			super(meta);
		}
	}

}
