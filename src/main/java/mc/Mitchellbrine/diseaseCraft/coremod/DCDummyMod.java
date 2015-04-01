package mc.Mitchellbrine.diseaseCraft.coremod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DCDummyMod extends DummyModContainer {

	public DCDummyMod() {
		super(new ModMetadata());
		getMetadata().modId = "DCModLoader";
		getMetadata().name = "DCModuleLoader";
		getMetadata().version = "1.0";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void constrcution(FMLConstructionEvent event) {
		ClassHelper.searchForModules(event.getASMHarvestedData());
		try {
			File diseaseConfig = new File((File)FMLInjectionData.data()[6],"dcVersion.txt");

			double version = 2.0;

			if (diseaseConfig.exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(diseaseConfig)));
				String s;

				while ((s = reader.readLine()) != null) {
					version = Double.parseDouble(s);
				}

				reader.close();

				diseaseConfig.delete();
			}

			InputStream stream = new URL("https://raw.githubusercontent.com/Mitchellbrine/DiseaseCraft-2.0/master/version.txt").openStream();

			String writeToVersion = "";

			try {
				writeToVersion = IOUtils.toString(stream);
			} finally {
				stream.close();
			}

			diseaseConfig.createNewFile();
			PrintWriter writer = new PrintWriter(diseaseConfig);
			writer.println(writeToVersion);
			writer.close();

			double newVersion = 2.0;

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(diseaseConfig)));

			String s;

			while ((s = reader.readLine()) != null) {
				newVersion = Double.parseDouble(s);
			}

			reader.close();

			DiseaseCraft.shouldUpdate = newVersion > version;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}