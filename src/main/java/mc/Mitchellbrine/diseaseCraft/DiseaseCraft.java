package mc.Mitchellbrine.diseaseCraft;

import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.entity.EntityRegistration;
import mc.Mitchellbrine.diseaseCraft.event.ContractingEvents;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseManager;
import mc.Mitchellbrine.diseaseCraft.proxy.CommonProxy;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

	@Mod.Instance
	public static DiseaseCraft instance;

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

		/*

		THE FOLLOWING WAS TEST CODE!

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

		registerAllEvents();
		EntityRegistration.init();
		proxy.registerStuff();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DiseaseManager.readAllJSONs();
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
