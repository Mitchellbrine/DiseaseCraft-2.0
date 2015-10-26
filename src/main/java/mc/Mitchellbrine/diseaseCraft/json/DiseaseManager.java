package mc.Mitchellbrine.diseaseCraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import cpw.mods.fml.relauncher.FMLInjectionData;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseManager {

	private static List<String> files = new ArrayList<String>();
	private static File diseaseFolder = new File((File) FMLInjectionData.data()[6],"DiseaseCraft/");

	public static void readAllJSONs() {
		for (String file : files) {
			readJSON(file);
		}
	}

	private static void readJSON(String fileName) {

		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Disease.class, new DiseaseJSON());
			Gson gson = builder.create();

			File file = new File(fileName);

			Disease[] diseases = gson.fromJson(new FileReader(file), Disease[].class);

			for (Disease disease : diseases) {
				disease.addDomain(file.getName().replaceAll(".json", ""));
				Diseases.registerDisease(disease);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void findAllDiseases() {
		if (!diseaseFolder.exists()) {
			diseaseFolder.mkdirs();
		}
		if (diseaseFolder.listFiles() == null) { return; }
		for (File diseaseJSON : diseaseFolder.listFiles()) {
			if (diseaseJSON != null && diseaseJSON.getName().endsWith(".json")) {
				files.add(diseaseJSON.getPath());
				DiseaseCraft.logger.info("Found file " + diseaseJSON.getName() + " and added it to the list.");
			}
		}
	}

}
