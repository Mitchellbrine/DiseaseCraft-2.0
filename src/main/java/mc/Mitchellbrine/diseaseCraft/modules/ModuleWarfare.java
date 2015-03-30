package mc.Mitchellbrine.diseaseCraft.modules;

import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.IModule;
import mc.Mitchellbrine.diseaseCraft.modules.bioWar.item.ChemicalExtractor;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.item.Item;

/**
 * Created by Mitchellbrine on 2015.
 */
@DCModule(id = "bioWarfare",modid = "DiseaseCraft",dcVersion = References.VERSION, canBeDisabled = true)
public class ModuleWarfare implements IModule{

	public static Item chemicalExtractor;

	@Override
	public void preInit() {
		chemicalExtractor = new ChemicalExtractor();
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
