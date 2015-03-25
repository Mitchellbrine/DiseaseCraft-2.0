package mc.Mitchellbrine.diseaseCraft.utils;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

import java.util.ArrayList;

/**
 * Created by Mitchellbrine on 2015.
 */
public class StatHelper {

	@SuppressWarnings("unchecked")
	public static StatBase getStatBaseFromName(String name) {
		StatBase stat = null;
		for (StatBase statBase : (ArrayList<StatBase>)StatList.allStats) {
			if (statBase.statId.equalsIgnoreCase(name)) {
				stat = statBase;
				break;
			}
		}
		return stat;
	}

}
