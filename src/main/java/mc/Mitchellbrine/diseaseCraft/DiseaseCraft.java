package mc.Mitchellbrine.diseaseCraft;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import mc.Mitchellbrine.diseaseCraft.client.gui.GuiHandler;
import mc.Mitchellbrine.diseaseCraft.config.ConfigRegistry;
import mc.Mitchellbrine.diseaseCraft.dio.DiseaseDownloader;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.entity.EntityRegistration;
import mc.Mitchellbrine.diseaseCraft.event.ContractingEvents;
import mc.Mitchellbrine.diseaseCraft.item.ItemRegistry;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseManager;
import mc.Mitchellbrine.diseaseCraft.network.PacketHandler;
import mc.Mitchellbrine.diseaseCraft.proxy.CommonProxy;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Created by Mitchellbrine on 2015.
 */
@SuppressWarnings("unchecked")
@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
public class DiseaseCraft {

	@SidedProxy(clientSide = "mc.Mitchellbrine.diseaseCraft.proxy.ClientProxy", serverSide = "mc.Mitchellbrine.diseaseCraft.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger = LogManager.getLogger(References.MODID);

	public static boolean shouldUpdate = false;

	public static final double MC_VERSION = 7.10;

	@Mod.Instance
	public static DiseaseCraft instance;

	public DiseaseCraft() {
		new ClassHelper();
	}

	/**
	 * The order in disease loading is such:
	 *
	 * Pre-init: Register all the JSON diseases
	 * Init: Allow mods to register their diseases in code
	 * Post-init: Everything Disease related should be finished processing.
	 *
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ConfigRegistry.init(event.getModConfigurationDirectory());
		ConfigRegistry.doConfig(new Configuration(event.getSuggestedConfigurationFile()));


		proxy.registerStuff();

		if (DiseaseCraft.shouldUpdate && ConfigRegistry.autoUpdate) {
			DiseaseDownloader.init();
		}

		/*

		THE FOLLOWING WAS TEST CODE!

		try {
			Class clazz = Class.forName("mc.Mitchellbrine.diseaseCraft.DiseaseCraft");
			clazz.getMethod("hello").setAccessible(true);
			Method method = clazz.getMethod("hello");
			if (method.getGenericReturnType() == Boolean.TYPE) {
				boolean worked = (Boolean)method.invoke(this);
				System.out.println(worked);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Disease test = new Disease("testDisease");

		Disease domainTest = new Disease("blahblah:testTwo");

		Disease newerTest = new Disease("blahTwo:newer");
		newerTest.setMinimumVersion(3.0);

		Disease effectTest = new Disease("blahThree:effect");
		for (int i = 1; i <= 16;i++) {
			effectTest.addEffect(i);
		}

		Diseases.registerDisease(test);
		Diseases.registerDisease(domainTest);
		Diseases.registerDisease(newerTest);
		Diseases.registerDisease(effectTest);

		System.out.println(test.isRequirementMet());
		System.out.println(test);

		System.out.println(domainTest.isRequirementMet());
		System.out.println(domainTest);

		System.out.println(newerTest.isRequirementMet());
		System.out.println(newerTest);

		System.out.println(effectTest.isRequirementMet());
		System.out.println(effectTest); */

		new Diseases();

		DiseaseManager.findAllDiseases();

		ConfigRegistry.STATE = 0;
		ConfigRegistry.triggerState();

		registerAllEvents();
		EntityRegistration.init();
		ItemRegistry.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(DiseaseCraft.instance, new GuiHandler());
		PacketHandler.init();

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ConfigRegistry.triggerState();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DiseaseManager.readAllJSONs();
		ConfigRegistry.triggerState();
	}

	@Mod.EventHandler
	public void server(FMLServerStartingEvent event) {
		ConfigRegistry.triggerState();
	}

	public boolean hello() {
		System.out.println("Hello world!");
		return true;
	}

	private void registerAllEvents() {
		MinecraftForge.EVENT_BUS.register(new ContractingEvents());
		FMLCommonHandler.instance().bus().register(new ContractingEvents());
	}
}
