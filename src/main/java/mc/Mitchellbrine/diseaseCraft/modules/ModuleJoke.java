package mc.Mitchellbrine.diseaseCraft.modules;

import cpw.mods.fml.common.FMLCommonHandler;
import mc.Mitchellbrine.diseaseCraft.api.DCModule;
import mc.Mitchellbrine.diseaseCraft.api.Module;
import mc.Mitchellbrine.diseaseCraft.modules.jokeDisease.JokeEvents;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Mitchellbrine on 2015.
 */
@DCModule(id = "jokeDisease",modid = "DiseaseCraft",dcVersion = "2.0",canBeDisabled = true,isEnabled = false)
public class ModuleJoke extends Module{

	public static DamageSource shrekDamage = new DamageSource("shrek").setDamageBypassesArmor().setDifficultyScaled();

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new JokeEvents());
		FMLCommonHandler.instance().bus().register(new JokeEvents());
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
