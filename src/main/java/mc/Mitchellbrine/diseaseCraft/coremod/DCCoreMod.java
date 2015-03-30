package mc.Mitchellbrine.diseaseCraft.coremod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;

import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
@IFMLLoadingPlugin.Name("DC-ModLoader")
public class DCCoreMod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return "mc.Mitchellbrine.diseaseCraft.coremod.DCDummyMod";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}


	public class DCDummyMod extends DummyModContainer {

		public DCDummyMod() {
			super(new ModMetadata());
		}

		@Override
		public boolean registerBus(EventBus bus, LoadController controller) {
			bus.register(this);
			return true;
		}

		@Subscribe
		public void constrcution(FMLConstructionEvent event) {
			ClassHelper.searchForModules(event.getASMHarvestedData());
		}

	}

}
