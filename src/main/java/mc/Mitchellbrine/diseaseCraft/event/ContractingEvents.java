package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.JsonPrimitive;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import net.minecraft.entity.EntityList;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ContractingEvents {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (event.source != null && event.source.getEntity() != null && EntityList.classToStringMapping != null && disease.getWaysToContract() != null && EntityList.getEntityString(event.source.getEntity()) != null && disease.getParameters("mobAttack") != null && disease.getWaysToContract().contains("mobAttack") && ((JsonPrimitive)disease.getParameters("mobAttack")[0]).getAsString().replaceAll("\"", "").equalsIgnoreCase(EntityList.getEntityString(event.source.getEntity()))) {
				System.out.println("Disease Event fired without random!");
				GenericEffects.rand.setSeed(event.entityLiving.worldObj.getTotalWorldTime());
				if (GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive)disease.getParameters("mobAttack")[1]).getAsInt()) {
					System.out.println("Got disease " + disease.getId());
				}
			} else if (event.source != null && event.source.getEntity() != null && disease.getWaysToContract() != null && disease.getWaysToContract().contains("mobAttack")) {
				System.out.println("The entity is not correct!");

			}
		}
	}

}
