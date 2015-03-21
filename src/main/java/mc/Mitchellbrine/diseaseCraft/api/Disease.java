package mc.Mitchellbrine.diseaseCraft.api;

import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.utils.References;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
public class Disease {

	private String identifier;
	private String domain;

	private List<String> waysToContract;
	private Map<String,Object[]> contractParameters;

	public List<Integer> effects;

	public int level;

	private double minimumRequiredVersion;

	public Disease(String id) {
		this.domain = "DiseaseCraft";
		this.identifier = id;
		if (id.contains(":")) {
			this.domain = id.substring(0,id.indexOf(":"));
			this.identifier = id.substring(id.indexOf(":") + 1);
		}

		waysToContract = new ArrayList<String>();
		contractParameters = new HashMap<String, Object[]>();

		effects = new ArrayList<Integer>();

		this.minimumRequiredVersion = Double.parseDouble(References.VERSION);
	}

	public String getId() {
		return this.identifier;
	}

	public String getUnlocalizedName() {
		boolean hasOwner = !domain.equalsIgnoreCase("DiseaseCraft");
		return "disease." + (hasOwner ? domain + "." : "") + this.identifier + ".name";
	}

	public void addEffect(int effect) {
		effects.add(effect);
	}

	public void setMinimumVersion(double douvar) {
		this.minimumRequiredVersion = douvar;
	}

	public void addDomain(String domain) {
		if (this.domain == null) {
			this.domain = domain;
		}
	}

	public boolean isRequirementMet() {
			if (Double.parseDouble(References.VERSION) >= this.minimumRequiredVersion) {
				return true;
			}
		DiseaseCraft.logger.warn("The disease [" + this.identifier + "] from the mod [" + this.domain + "] has not met it's requirement of [" + this.minimumRequiredVersion + "], disabling!");
		return false;
	}

	public List<Integer> getEffects() {
		return this.effects;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public void addWayToContract(String type, Object[] parameters) {
		waysToContract.add(type);
		contractParameters.put(type,parameters);
	}

	public void removeWayToContract(String type) {
		if (waysToContract.contains(type) && contractParameters.containsKey(type))
			waysToContract.remove(type);
			contractParameters.remove(type);
	}

	public Object[] getParameters(String type) {
		return contractParameters.get(type);
	}

	public List<String> getWaysToContract() {
		return waysToContract;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int effect : effects) {
			builder.append(effect + "\n");
		}
		return "Disease (mc.Mitchellbrine.diseaseCraft.api.Disease):\n\nDisease Name: " + this.identifier + "\nDisease Domain: " + this.domain + "\nMinimum required version: " + this.minimumRequiredVersion + "\n" + (!effects.isEmpty() ? "Effects:\n" + builder.toString() : "");
	}
}
