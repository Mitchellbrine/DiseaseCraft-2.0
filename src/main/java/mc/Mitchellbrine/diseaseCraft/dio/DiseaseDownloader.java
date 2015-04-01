package mc.Mitchellbrine.diseaseCraft.dio;

import cpw.mods.fml.relauncher.FMLInjectionData;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseDownloader {

	public static void init() {
		try {
			PrintWriter writer = new PrintWriter(new File((File)FMLInjectionData.data()[6],"DiseaseCraft/DiseaseCraft.json"));
			InputStream stream = new URL("https://raw.githubusercontent.com/Mitchellbrine/DiseaseCraft-2.0/master/diseaseJSON/DiseaseCraft.json").openStream();

			try {
				writer.println(IOUtils.toString(stream));
			} finally {
				stream.close();
			}

			writer.close();

			PrintWriter writer1 = new PrintWriter(new File((File)FMLInjectionData.data()[6],"dcVersion.json"));
			InputStream stream1 = new URL("https://raw.githubusercontent.com/Mitchellbrine/DiseaseCraft-2.0/master/version.json").openStream();

			try {
				writer1.println(IOUtils.toString(stream1));
			} finally {
				stream1.close();
			}
			writer1.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		VersionJSON.init();
	}

}
