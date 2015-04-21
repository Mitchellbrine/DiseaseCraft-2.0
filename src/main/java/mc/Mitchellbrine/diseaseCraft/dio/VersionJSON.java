package mc.Mitchellbrine.diseaseCraft.dio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.relauncher.FMLInjectionData;
import java.util.Arrays;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class VersionJSON {

	public static List<DCVersion> versions;

	@SuppressWarnings("unchecked")
	public static void init() {

		versions = new ArrayList<DCVersion>();

		try {
			File file = new File((File) FMLInjectionData.data()[6],"dcVersion.json");
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(DCVersion.class, new DCVersionJSON());
			Gson gson = builder.create();

			DCVersion[] diseases = gson.fromJson(new FileReader(file), DCVersion[].class);

			versions = Arrays.asList(diseases);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static DCVersion getVersion(double versionNumber) {
		for (DCVersion version : versions) {
			if (version.versionNumber == versionNumber)
				return version;
		}
		return null;
	}


}
