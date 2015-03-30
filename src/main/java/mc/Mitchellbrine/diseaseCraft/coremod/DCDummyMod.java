package mc.Mitchellbrine.diseaseCraft.coremod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DCDummyMod extends DummyModContainer {

	public DCDummyMod() {
		super(new ModMetadata());
		getMetadata().modId = "DCModLoader";
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