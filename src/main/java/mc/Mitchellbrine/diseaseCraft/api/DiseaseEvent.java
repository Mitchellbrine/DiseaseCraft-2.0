package mc.Mitchellbrine.diseaseCraft.api;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseEvent extends LivingEvent {

	public Disease disease;

	public DiseaseEvent(Disease disease, EntityLivingBase entity) {
		super(entity);
		this.disease = disease;
	}

	@Cancelable
	public static class DiseaseEffectEvent extends DiseaseEvent {

		public DiseaseEffectEvent(Disease disease, EntityLivingBase entity) {
			super(disease, entity);
		}
	}

	public static class DiseaseTickEvent extends DiseaseEvent {

		public DiseaseTickEvent(Disease disease, EntityLivingBase entity) { super(disease, entity); }
	}

	public static class DiseaseEndEvent extends DiseaseEvent {

		public DiseaseEndEvent(Disease disease, EntityLivingBase entity) {
			super(disease, entity);
		}

	}

}
