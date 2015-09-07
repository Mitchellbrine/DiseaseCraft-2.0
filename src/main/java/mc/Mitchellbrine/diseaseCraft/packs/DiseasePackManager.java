package mc.Mitchellbrine.diseaseCraft.packs;

import cpw.mods.fml.relauncher.FMLInjectionData;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Mitchellbrine on 2015.
 */
public class DiseasePackManager {


	private static List<String> packs;
	private static File packFolder = new File((File) FMLInjectionData.data()[6],"DiseaseCraft/packs/");

	public static void findAllPacks() {
		packs = new ArrayList<String>();
		try {
			if (!packFolder.exists()) {
				packFolder.createNewFile();
			}
			if (packFolder.listFiles() == null) { return; }
			for (File pack : packFolder.listFiles()) {
				if (pack != null && pack.getName().endsWith(".zip")) {
					packs.add(pack.getPath());
				}
			}
			loadAllPacks();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadAllPacks() {
		for (String packName : packs) {
			File packFile = new File(packName);

			try {
				ZipFile zip = new ZipFile(packFile);

				final Enumeration<? extends ZipEntry> entries = zip.entries();
				while ( entries.hasMoreElements() )
				{
					final ZipEntry entry = entries.nextElement();
					if (!entry.getName().endsWith(".json"))
						continue;
					if (entry.getName().startsWith("override_")) {
						if (DiseaseManager.jsonExists(entry.getName().replaceAll("override_",""))) {
							DiseaseManager.addOverride(zip,entry);
						} else {
							System.out.println(entry.getName().replaceAll("override_",""));
						}
					}
					if (entry.getName().startsWith("diseases/")) {
						if (DiseaseManager.jsonExists(entry.getName().replaceAll("override_",""))) {
							DiseaseCraft.logger.error("JSON file " + entry.getName().substring(entry.getName().lastIndexOf(File.separatorChar)) + " already exists, either rename or place in overrides/");
						} else {
							DiseaseManager.addOverride(zip,entry);
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
