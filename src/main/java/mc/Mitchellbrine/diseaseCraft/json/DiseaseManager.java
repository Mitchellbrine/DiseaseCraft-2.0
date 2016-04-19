package mc.Mitchellbrine.diseaseCraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.config.ConfigRegistry;
import mc.Mitchellbrine.diseaseCraft.dio.JSONDownloaderManager;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseaseManager {

	private static List<String> files = new ArrayList<String>();
	private static List<String> nativeFiles = new ArrayList<String>();
	private static List<ResourceLocation> nativeFileLoc = new ArrayList<ResourceLocation>();

	private static List<String> overrideNames = new ArrayList<String>();
	private static List<InputStream> overrideStreams = new ArrayList<InputStream>();

	public static File diseaseFolder = new File((File) FMLInjectionData.data()[6],"DiseaseCraft" + File.separator);

	private static DiseaseManager INST;

	public static DiseaseManager instance() {
		if (INST == null) {
			INST = new DiseaseManager();
		}
		return INST;
	}

	public static void readAllJSONs() {
			for (ResourceLocation nativeFile : nativeFileLoc) {
				try {
					if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
						readJSON(net.minecraft.client.Minecraft.getMinecraft().getResourceManager().getResource(nativeFile).getInputStream());
					} else {
						readJSON(instance().getClass().getClassLoader()
							.getResourceAsStream("assets" + File.separator + nativeFile.getResourceDomain() + File.separator + nativeFile.getResourcePath()));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		for (String file : files) {
			readJSON(file);
			/*
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
			}*/

		}
		/*
		for (InputStream stream : overrideStreams)
			readJSON(stream);
			*/
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

	public static void readStringJSON(String string) {

		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Disease.class, new DiseaseJSON());
			Gson gson = builder.create();

			Disease[] diseases = gson.fromJson(new StringReader(string), Disease[].class);

			for (Disease disease : diseases) {
				//disease.addDomain("");
				DiseaseCraft.logger.info(String.format("Registered the disease \"%s\" via a string",disease.getId()));
				Diseases.registerDisease(disease);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void readStringJSON(String string, String siteDomain, String URL) {

		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Disease.class, new DiseaseJSON());
			Gson gson = builder.create();

			Disease[] diseases = gson.fromJson(new StringReader(string), Disease[].class);

			for (Disease disease : diseases) {
				DiseaseCraft.logger.info(String.format("Registered the disease \"%s\" from the site %s",disease.getId(),siteDomain));
				disease.addDomain(URL.lastIndexOf('/') > -1 ? URL.substring(URL.lastIndexOf('/')) : URL);
				System.out.println(disease.getUnlocalizedName());
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
				if (overrideStreams.indexOf(fileName) == -1) {
					disease.addDomain("DiseaseCraft");
				} else {
					disease.addDomain(overrideNames.get(overrideStreams.indexOf(fileName)));
				}
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
		if (ConfigRegistry.useNativeDiseases) {
			nativeFiles.add("diseasecraft.json");
			nativeFileLoc.add(new ResourceLocation("diseasecraft:diseases/DiseaseCraft.json"));
			DiseaseCraft.logger.info("Added the native file. Here we go!");
		}
		if (diseaseFolder.listFiles() == null) { return; }
		for (File diseaseJSON : diseaseFolder.listFiles()) {
			if (diseaseJSON != null && !diseaseJSON.isDirectory() && diseaseJSON.getName().endsWith(".json")) {
				if (!jsonExists(diseaseJSON.getName())) {
					files.add(diseaseJSON.getPath());
					DiseaseCraft.logger.info("Found file " + diseaseJSON.getName() + " and added it to the list.");
				} else {
					if (!isNative(diseaseJSON.getName())) {
						files.remove(jsonFile(diseaseJSON.getName()));
					} else {
						nativeFileLoc.remove(nativeFiles.indexOf(diseaseJSON.getName().toLowerCase()));
						nativeFiles.remove(diseaseJSON.getName().toLowerCase());
					}
					files.add(diseaseJSON.getPath());
					DiseaseCraft.logger.info("Overrode a disease json file with " + diseaseJSON.getName());
				}
			} else if (diseaseJSON != null && !diseaseJSON.isDirectory() && diseaseJSON.getName().equalsIgnoreCase("DownloadedJSONs.cfg")) {
				try {
					JSONDownloaderManager.compileList();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static boolean jsonExists(String name) {
		for (String fileName : files) {
			if (fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1).equalsIgnoreCase(name))
				return true;
			else {
				//System.out.println("Separator Char: " + File.separatorChar + " | Path Separator Char: " + File.pathSeparatorChar);
				//System.err.println(fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1));
			}
		}
		for (String nativeFile : nativeFiles) {
			if (nativeFile.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	public static String jsonFile(String name) {
		for (String fileName : files) {
			if (fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1).equalsIgnoreCase(name))
				return fileName;
			/*else {
				System.out.println("Separator Char: " + File.separatorChar + " | Path Separator Char: " + File.pathSeparatorChar);
				System.err.println(fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1));
			}*/
		}
		for (String nativeFile : nativeFiles) {
			if (nativeFile.equalsIgnoreCase(name))
				return nativeFile;
		}
		return "missingno";
	}

	public static boolean isNative(String name) {
		for (String nativeFile : nativeFiles) {
			if (nativeFile.equalsIgnoreCase(name))
				return true;
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
			/*else
				System.out.println(fileName + " | " + name);*/
		}
		return false;
	}

	private static InputStream getOverride(String name) {
		InputStream stream = overrideStreams.get(overrideNames.indexOf(name));
		return stream;
	}

}
