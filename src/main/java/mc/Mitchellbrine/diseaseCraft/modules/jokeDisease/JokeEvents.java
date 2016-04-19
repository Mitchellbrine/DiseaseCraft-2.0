package mc.Mitchellbrine.diseaseCraft.modules.jokeDisease;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import mc.Mitchellbrine.diseaseCraft.modules.ModuleJoke;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Created by Mitchellbrine on 2015.
 */
public class JokeEvents {

	@SubscribeEvent
	public void livingUpdate(DiseaseEvent.DiseaseTickEvent event) {
		if (DiseaseHelper.diseaseExists("shrekRabies") && event.disease == DiseaseHelper.getDiseaseInstance("shrekRabies")) {
			int posX = MathHelper.floor_double(event.entityLiving.posX);
			int posY = MathHelper.floor_double(event.entityLiving.posY);
			int posZ = MathHelper.floor_double(event.entityLiving.posZ);

			if ((event.entityLiving.worldObj.getBlock(posX, posY, posZ).getMaterial() == Material.water || event.entityLiving.worldObj.getBlock(posX,posY-1,posZ).getMaterial() == Material.water) && event.entityLiving.worldObj.getBiomeGenForCoordsBody(posX,posZ) != BiomeGenBase.swampland) {
				if (event.entityLiving.worldObj.getTotalWorldTime() % 20 == 0) {
					event.entityLiving.attackEntityFrom(ModuleJoke.shrekDamage,1.0F);
					if (!event.entityLiving.worldObj.isRemote) {
						if (GenericEffects.rand.nextInt(3) == 0)
							event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "DiseaseCraft:mySwamp", 1.0f, 1.0f);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void death(LivingDeathEvent event) {
		if (event.source == ModuleJoke.shrekDamage) {
			if (GenericEffects.rand.nextInt(2) == 0) {
				event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "DiseaseCraft:neverOgre", 1.0f, 1.0f);
			} else {
				event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "DiseaseCraft:allOgre", 1.0f, 1.0f);
			}
		}
	}

}
