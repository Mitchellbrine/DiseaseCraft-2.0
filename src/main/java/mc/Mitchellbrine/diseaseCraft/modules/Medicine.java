package mc.Mitchellbrine.diseaseCraft.modules;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import mc.Mitchellbrine.diseaseCraft.modules.med.item.ItemMedication;
import mc.Mitchellbrine.diseaseCraft.modules.med.recipe.MedicationRecipes;
import mc.Mitchellbrine.diseaseCraft.modules.med.util.MedUtils;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mitchellbrine on 2015.
 */
@DCModule(id = "medication",modid = "DiseaseCraft",version = "1.0",dcVersion = References.VERSION,canBeDisabled = true)
public class Medicine extends Module {

	public static Logger logger = LogManager.getLogger("DC-Medicine");

	public static Item medication;

	public static CreativeTabs tab;

	@Override
	public void preInit() {
		tab = new CreativeTabs(CreativeTabs.getNextID(),"DCMedication") {
			@Override
			public Item getTabIconItem() {
				return medication;
			}
		};

		medication = new ItemMedication();
		MinecraftForge.EVENT_BUS.register(new MedUtils());
		FMLCommonHandler.instance().bus().register(new MedUtils());
		GameRegistry.registerItem(medication, "medication");


		MedicationRecipes.addMedicationType(new ItemStack(Items.glowstone_dust),"parkinsons","Carbodopa-Levidopa",3);
		MedicationRecipes.addMedicationType(new ItemStack(Items.redstone),"influenza","Acetaminophen",1);
		MedicationRecipes.addMedicationType(new ItemStack(Items.gunpowder),"malaria","Chloroquine",4);

		MedicationRecipes.init();
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}

	@Override
	public void serverStart() {
	}
}
