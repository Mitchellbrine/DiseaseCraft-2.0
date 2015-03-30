package mc.Mitchellbrine.diseaseCraft.api;

/**
 * Created by Mitchellbrine on 2015.
 */
public interface IModule {

	public void preInit();

	public void init();

	public void postInit();

	public void serverStart();

}
