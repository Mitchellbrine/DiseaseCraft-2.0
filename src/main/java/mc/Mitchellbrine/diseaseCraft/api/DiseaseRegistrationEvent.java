package mc.Mitchellbrine.diseaseCraft.api;

import com.google.gson.JsonElement;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2016.
 */
public class DiseaseRegistrationEvent extends Event {

	public final Disease disease;

	public DiseaseRegistrationEvent(Disease disease){
		this.disease = disease;
	}

	public static class Override extends DiseaseRegistrationEvent {

		public int[] effects = null;
		public int level = -1;
		public Map<String,JsonElement[]> contractionMap = null;
		public int deathRate = -127;
		public Boolean isJoke = null;
		public String lore = "missingno1234598765";

		public Override(Disease disease) {
			super(disease);
		}
	}

	@Cancelable
	/**
	 * This method is your way of preventing certain methods from being registered to certain numbers. USE WITH *EXTREME* CAUTION! This ultimately WILL affect gameplay and will make MANY people upset.
	 *
	 * I, Mitchellbrine, have absolutely no liability in what anybody does with this method (as stated in DiseaseCraft's license).
	 */
	public static class Mode extends Event {
		public final int modeNumber;
		public final Method method;

		public Mode(int number, Method methodPointer) {
			modeNumber = number;
			method = methodPointer;
		}
	}

	@Cancelable
	/**
	 *	Use this to either alter a disease (you can only alter parts of the disease that you are able to access) or to cancel a disease from registering.
	 *
	 * 	If you alter the disease *and* cancel the event, the new altered disease won't be registered. Fair warning. Now you can continue.
	 *
	 */
	public static class DiseaseCancelation extends DiseaseRegistrationEvent {

		public DiseaseCancelation(Disease disease) {
			super(disease);
		}
	}

}
