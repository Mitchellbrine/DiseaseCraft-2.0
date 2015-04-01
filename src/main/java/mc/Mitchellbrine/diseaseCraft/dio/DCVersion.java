package mc.Mitchellbrine.diseaseCraft.dio;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DCVersion {

	public double versionNumber;
	public double mcVersion;
	public String updateString;

	public DCVersion(double versionNumber, double mcVersion, String updateString) {
		this.versionNumber = versionNumber;
		this.mcVersion = mcVersion;
		this.updateString = updateString;
	}
}
