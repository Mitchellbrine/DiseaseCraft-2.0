package mc.Mitchellbrine.diseaseCraft.dio;

import cpw.mods.fml.relauncher.FMLInjectionData;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2016.
 */
public class JSONDownloaderManager {

	public static Map<String, String> urls;
	public static File downloadedCfgs = new File((File) FMLInjectionData.data()[6],"DiseaseCraft/DownloadedJSONs.cfg");

	public static void compileList() throws IOException {
		if (downloadedCfgs.exists()) {

			urls = new HashMap<String, String>();

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(downloadedCfgs)));

			while (reader.readLine() != null) {
				String line = reader.readLine();
				if (line != null) {
					System.out.println(line);
					System.out.println(line.substring(0, line.indexOf(":")));
					System.out.println(line.substring(line.indexOf(":")));
					urls.put(line.substring(0, line.indexOf(":")), line.substring(line.indexOf(":")));
				}
			}

			reader.close();
		}
	}


	private static String getUrlContent(final String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		conn.setRequestMethod("GET");
		conn.setInstanceFollowRedirects(true);
		conn.setReadTimeout(3000);
		conn.setConnectTimeout(3000);

		final int code = conn.getResponseCode();
		if (HttpURLConnection.HTTP_OK != code) {
			DiseaseCraft.logger.error(String.format("Got HTTP response code %d from URL for %s", code, urlStr));
			return null;
		}
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder builder = new StringBuilder(4096);
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append('\n');
		}
		reader.close();
		isr.close();
		is.close();

		return builder.toString();
	}

	public static void readList() {

		for (String key : urls.keySet()) {
			if (key.equalsIgnoreCase("GitHub")) {
				String gitHubSlug = urls.get(key);
				gitHubSlug = gitHubSlug.replaceAll("https://github.com/", "https://raw.githubusercontent.com/");
				gitHubSlug = gitHubSlug.replaceAll("https://www.github.com/", "https://www.raw.githubusercontent.com/");
				gitHubSlug = gitHubSlug.replaceAll("/blob/", "/");

				try {
					String string = getUrlContent(gitHubSlug);
					DiseaseManager.readStringJSON(string, "GitHub", gitHubSlug);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else if (key.equalsIgnoreCase("DiseaseCraft.com")) {
				// Doesn't exist yet
			} else if (key.equalsIgnoreCase("Pastebin")) {
				String slug = urls.get(key);
				slug = slug.replaceAll("http://pastebin.com/", "");
				slug = slug.replaceAll("http://www.pastebin.com/", "");
				slug = slug.replaceAll("https://pastebin.com/", "");
				slug = slug.replaceAll("https://www.pastebin.com/", "");

				try {
					String string = getUrlContent("http://pastebin.com/raw.php?i=" + slug);
					DiseaseManager.readStringJSON(string, "Pastebin", urls.get(key));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else if (key.equalsIgnoreCase("Raw")) {
				try {
					String string = getUrlContent(urls.get(key));
					DiseaseManager.readStringJSON(string, "Raw", urls.get(key));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

				/*

				switch (choice) {
					case 1:
						// GitHub
						String gitHubSlug = "" + textField.getText();
						gitHubSlug = gitHubSlug.replaceAll("https://github.com/", "https://raw.githubusercontent.com/");
						gitHubSlug = gitHubSlug.replaceAll("https://www.github.com/", "https://www.raw.githubusercontent.com/");
						gitHubSlug = gitHubSlug.replaceAll("/blob/", "/");

						try {
							String string = getUrlContent(gitHubSlug);
							DiseaseManager.readStringJSON(string, "GitHub", textField.getText());
							if (!downloadedCfgs.exists()) {
								downloadedCfgs.createNewFile();
							}
							writeURL("GitHub",textField.getText());
						} catch (IOException ex) {
							//System.out.println(gitHubSlug);
							ex.printStackTrace();
						}
						break;
					if ()
						// DiseaseCraft.com
						break;
					case 3:
						// Pastebin
						String slug = "" + textField.getText();
						slug = slug.replaceAll("http://pastebin.com/", "");
						slug = slug.replaceAll("http://www.pastebin.com/", "");

						try {
							String string = getUrlContent("http://pastebin.com/raw.php?i=" + slug);
							DiseaseManager.readStringJSON(string, "Pastebin", textField.getText());
							if (!downloadedCfgs.exists()) {
								downloadedCfgs.createNewFile();
							}
							writeURL("Pastebin",textField.getText());
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						break;
					case 4:
						try {
							String string = getUrlContent(textField.getText());
							DiseaseManager.readStringJSON(string, "Raw", textField.getText());
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						break;
				}
				 */
	}


}
