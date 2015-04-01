package mc.Mitchellbrine.diseaseCraft.api;

/**
 * Created by Mitchellbrine on 2015.
 */
public abstract class Module {

	public abstract void preInit();

	public abstract void init();

	public abstract void postInit();

	public abstract void serverStart();

}
