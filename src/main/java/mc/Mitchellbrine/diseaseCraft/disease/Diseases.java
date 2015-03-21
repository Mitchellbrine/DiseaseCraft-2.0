package mc.Mitchellbrine.diseaseCraft.disease;

import mc.Mitchellbrine.diseaseCraft.api.Disease;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class Diseases {

	public static List<Disease> diseases;
	public static List<Integer> acceptableModes;

	static {
		diseases = new ArrayList<Disease>();
		acceptableModes = new ArrayList<Integer>();
		acceptableModes.add(-1 /* Jitter */);
		acceptableModes.add(-2 /* Drop Item */);
		acceptableModes.add(-3 /* Hydrophobia */);
		acceptableModes.add(-4 /* *RESERVED* */);
		acceptableModes.add(-5 /* *RESERVED* */);
		acceptableModes.add(-6 /* *RESERVED* */);
	}

	@SuppressWarnings("unchecked")
	public static void registerDisease(Disease disease) {
		if (disease.isRequirementMet()) {
			ArrayList<Integer> effects = (ArrayList<Integer>)disease.effects;
			ArrayList<Integer> correctEffects = new ArrayList<Integer>();

			for (int effect : effects) {
				if (effect < 0 || Potion.potionTypes[effect] != null)
				correctEffects.add(effect);
			}

			disease.effects = correctEffects;

			diseases.add(disease);
		}
	}

}
