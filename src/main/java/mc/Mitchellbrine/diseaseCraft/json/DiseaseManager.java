package mc.Mitchellbrine.diseaseCraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import cpw.mods.fml.relauncher.FMLInjectionData;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseManager {

	private static List<String> files = new ArrayList<String>();

	private static List<String> overrideNames = new ArrayList<String>();
	private static List<InputStream> overrideStreams = new ArrayList<InputStream>();

	private static File diseaseFolder = new File((File) FMLInjectionData.data()[6],"DiseaseCraft/");

	public static void readAllJSONs() {
		for (String file : files) {
			if (!overrideExists(file.substring(file.lastIndexOf(File.separatorChar) + 1))) {
				System.out.println(file + " | " + file.substring(file.lastIndexOf(File.separatorChar) + 1));
				readJSON(file);
			} else {
				System.out.println(file + " | " + file.substring(file.lastIndexOf(File.separatorChar) + 1));
				DiseaseCraft.logger.info("Found override for " + file.substring(file.lastIndexOf(File.separatorChar) + 1) + "! Overriding!");
				InputStream stream = getOverride(file.substring(file.lastIndexOf(File.separatorChar) + 1));
				readJSON(stream);
				System.out.println(file.substring(file.lastIndexOf(File.separatorChar) + 1));
				overrideNames.remove(file.substring(file.lastIndexOf(File.separatorChar) + 1));
				// This is where we seems to have the most trouble
				overrideStreams.remove(stream);
			}
		}
		for (InputStream stream : overrideStreams)
			readJSON(stream);
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

	private static void readJSON(InputStream fileName) {

		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Disease.class, new DiseaseJSON());
			Gson gson = builder.create();

			Disease[] diseases = gson.fromJson(new InputStreamReader(fileName), Disease[].class);

			for (Disease disease : diseases) {
				disease.addDomain(overrideNames.get(overrideStreams.indexOf(fileName)));
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

	public static boolean jsonExists(String name) {
		for (String fileName : files) {
			if (fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1).equalsIgnoreCase(name))
				return true;
			else {
				System.out.println("Separator Char: " + File.separatorChar + " | Path Separator Char: " + File.pathSeparatorChar);
				System.err.println(fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1));
			}
		}
		return false;
	}

	public static void addOverride(ZipFile file, ZipEntry entry) {
		try {
			overrideStreams.add(file.getInputStream(entry));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		overrideNames.add(entry.getName().replaceAll("override_", ""));
	}

	public static boolean overrideExists(String name) {
		for (String fileName : overrideNames) {
			if (fileName.equalsIgnoreCase(name))
				return true;
			else
				System.out.println(fileName + " | " + name);
		}
		return false;
	}

	private static InputStream getOverride(String name) {
		InputStream stream = overrideStreams.get(overrideNames.indexOf(name));
		return stream;
	}

}
