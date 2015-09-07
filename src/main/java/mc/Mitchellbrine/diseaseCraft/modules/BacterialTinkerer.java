package mc.Mitchellbrine.diseaseCraft.modules;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import mc.Mitchellbrine.diseaseCraft.modules.bacTink.block.BacStation;
import mc.Mitchellbrine.diseaseCraft.modules.bacTink.client.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Mitchellbrine on 2015.
 */
@DCModule(id = "bacTink",modid = "DiseaseCraft",version = "1.0",dcVersion = "2.2",canBeDisabled = true, requiredModules = "bioWarfare", isEnabled = false)
public class BacterialTinkerer extends Module{

	@SidedProxy(clientSide = "mc.Mitchellbrine.diseaseCraft.modules.bacTink.client.ClientProxy",serverSide = "mc.Mitchellbrine.diseaseCraft.modules.bacTink.client.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tab;

	public static Block bacTinkStation;

	@Mod.Instance("bacTink")
	public static BacterialTinkerer instance;

	@Override
	public void preInit() {
		proxy.renderStuff();

		tab = new CreativeTabs(CreativeTabs.getNextID(),"bacTink") {
			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(bacTinkStation);
			}
		};

		bacTinkStation = new BacStation();

		GameRegistry.registerBlock(bacTinkStation,"bacStation");
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
