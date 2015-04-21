package mc.Mitchellbrine.diseaseCraft.modules;

import cpw.mods.fml.common.FMLCommonHandler;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import mc.Mitchellbrine.diseaseCraft.modules.bioWar.event.ChemicalEvents;
import mc.Mitchellbrine.diseaseCraft.modules.bioWar.item.ChemicalExtractor;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Mitchellbrine on 2015.
 */
@DCModule(id = "bioWarfare",modid = "DiseaseCraft",dcVersion = References.VERSION, canBeDisabled = true)
public class ModuleWarfare extends Module {

	public static CreativeTabs tab;

	public static Item chemicalExtractor;

	public void preInit() {
		tab = new CreativeTabs(CreativeTabs.getNextID(),"bioWarfare") {
			@Override
			public Item getTabIconItem() {
				return chemicalExtractor;
			}

			@Override
			public ItemStack getIconItemStack() {
				return new ItemStack(chemicalExtractor,1,1);
			}
		};
		chemicalExtractor = new ChemicalExtractor().setUnlocalizedName("chemicalExtractor");
		MinecraftForge.EVENT_BUS.register(new ChemicalEvents());
		FMLCommonHandler.instance().bus().register(new ChemicalEvents());
	}

	public void init() {

	}

	public void postInit() {

	}

	public void serverStart() {

	}
}
