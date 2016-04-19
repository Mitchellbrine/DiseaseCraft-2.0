package mc.Mitchellbrine.diseaseCraft.client.gui;

import cpw.mods.fml.relauncher.FMLInjectionData;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.json.DiseaseManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GuiDiseaseMenu extends GuiScreen {

	private GuiMainMenu menu;

	private GuiButton github, pastebin, diseaseCraftSite, download, raw, back;
	private int choice;
	private GuiTextField textField;

	private boolean isMalformed = false;
	private String previousMalformed = "";
	private int previousButton = -1;
	private String cachedString = "missingno";

	public static File downloadedCfgs = new File((File)FMLInjectionData.data()[6],"DiseaseCraft/DownloadedJSONs.cfg");

	public GuiDiseaseMenu(GuiMainMenu oldMenu) {
		this.menu = oldMenu;
		buttonList = new ArrayList<GuiButton>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		textField = new GuiTextField(fontRendererObj,width / 2 - 100, height / 2 - 40,200,20);
		textField.setMaxStringLength(200);
		buttonList.clear();
		diseaseCraftSite = new GuiButton(2,width / 2 - 50,height / 2 + 20,100,20,"<RESERVED>");
		buttonList.add(diseaseCraftSite);
		raw = new GuiButton(4,width / 2 - 50, height / 2,100,20,"Raw");
		buttonList.add(raw);
		github = new GuiButton(1,(width / 2) - 100 - (diseaseCraftSite.width / 2),height / 2 + 20,100,20,"GitHub");
		buttonList.add(github);
		pastebin = new GuiButton(3,width / 2 + diseaseCraftSite.width / 2,height / 2 + 20,100,20,"Pastebin");
		buttonList.add(pastebin);
		download = new GuiButton(0,width / 2 - 100,height / 2 + 50,"Install");
		buttonList.add(download);
		back = new GuiButton(5,width / 2 - 50, height / 2 + 80,100,20,"Back");
		buttonList.add(back);

		choice = 4;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateScreen() {
		super.updateScreen();
		textField.updateCursorCounter();
		if (choice > 0)
			for (GuiButton button : ((List<GuiButton>)buttonList)) {
				if (button.id < 5)
					button.enabled = button.id != choice;
			}

		if (isMalformed) {
			download.enabled = false;
		}

		if (choice <= 0 || textField.getText().isEmpty())
			download.enabled = false;

		if (choice > 0 && !textField.getText().isEmpty() && !isMalformed)
			download.enabled = true;
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		textField.mouseClicked(x, y, button);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void drawScreen(int mouseX, int mouseY, float par3) {
		drawDefaultBackground();
		textField.drawTextBox();
		fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("diseaseCraft.jsonURL"), textField.xPosition + (textField.width / 2 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("diseaseCraft.jsonURL")) / 2), textField.yPosition - 12, 0xFFFFFF);
		fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("diseaseCraft.downloadSite"), raw.xPosition + (raw.width / 2 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("diseaseCraft.downloadSite")) / 2),raw.yPosition - 12, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, par3);
		checkMalformed(mouseX, mouseY);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
			case 0:
				if (choice > 0 && !textField.getText().isEmpty()) {
					if (cachedString != null && !cachedString.equalsIgnoreCase("missingno")) {
						String domain = "missingno";
						switch (choice) {
							case 1:
								domain = "GitHub";
								break;
							case 2:
								domain = "DiseaseCraft.com";
								break;
							case 3:
								domain = "Pastebin";
								break;
							case 4:
								domain = "Raw";
								break;
						}
						DiseaseManager.readStringJSON(cachedString, domain, textField.getText());
						if (!downloadedCfgs.exists()) {
							try {
								downloadedCfgs.createNewFile();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
						writeURL(domain,textField.getText());
					} else {
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
							case 2:
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
									if (!downloadedCfgs.exists()) {
										downloadedCfgs.createNewFile();
									}
									writeURL("Raw",textField.getText());
								} catch (IOException ex) {
									ex.printStackTrace();
								}
								break;
						}
					}
					Minecraft.getMinecraft().displayGuiScreen(menu);
				}
				break;
			case 5:
				Minecraft.getMinecraft().displayGuiScreen(menu);
				break;
			default:
				choice = button.id;
				break;
		}
	}

	@Override
	protected void keyTyped(char key, int p_73869_2_) {
		textField.textboxKeyTyped(key, p_73869_2_);
		super.keyTyped(key, p_73869_2_);
	}

	public void drawHoveringString(List list, int x, int y) {
		super.drawHoveringText(list, x, y, fontRendererObj);
	}

	private String getUrlContent(final String urlStr) throws IOException {
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

	private void writeURL(String domain, String URL) {
		try {

			String URLs = "";

				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(downloadedCfgs)));

				while (reader.readLine() != null) {
					URLs += reader.readLine() + "\n";
				}

			reader.close();

			System.out.println("\n" + URLs);

			PrintWriter writer = new PrintWriter(downloadedCfgs);
			for (String string : URLs.split("\n")) {
				if (!string.isEmpty())
					writer.println(string);
			}
			if (!URLs.contains(domain + ":" + URL))
				writer.println(domain + ":" + URL);
			writer.close();
			DiseaseCraft.logger.info("Finished writing the downloaded CFG file");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void checkMalformed(int mouseX, int mouseY) {
		if ((mouseX >= raw.xPosition && mouseX < (raw.xPosition + raw.width)) && (mouseY >= raw.yPosition && mouseY < (raw.yPosition + raw.height))) {
			List strings = new ArrayList();
			strings.add("Treat the link as the plain text file");
			drawHoveringString(strings,mouseX,mouseY);
		}
		if ((mouseX >= diseaseCraftSite.xPosition && mouseX < (diseaseCraftSite.xPosition + diseaseCraftSite.width)) && (mouseY >= diseaseCraftSite.yPosition && mouseY < (diseaseCraftSite.yPosition + diseaseCraftSite.height))) {
			List strings = new ArrayList();
			strings.add("Shh, coming soon!");
			drawHoveringString(strings,mouseX,mouseY);
		}
		if ((mouseX >= github.xPosition && mouseX < (github.xPosition + github.width)) && (mouseY >= github.yPosition && mouseY < (github.yPosition + github.height))) {
			List strings = new ArrayList();
			strings.add("Changes regular GitHub URL to the raw URL");
			drawHoveringString(strings,mouseX,mouseY);
		}
		if ((mouseX >= pastebin.xPosition && mouseX < (pastebin.xPosition + pastebin.width)) && (mouseY >= pastebin.yPosition && mouseY < (pastebin.yPosition + pastebin.height))) {
			List strings = new ArrayList();
			strings.add("Gets the raw text from a Pastebin URL");
			drawHoveringString(strings,mouseX,mouseY);
		}
		if ((mouseX >= download.xPosition && mouseX < (download.xPosition + download.width)) && (mouseY >= download.yPosition && mouseY < (download.yPosition + download.height))) {
			List strings = new ArrayList();
			//if (download.enabled) {
				if (!textField.getText().equalsIgnoreCase(previousMalformed) || previousButton != choice) {
					switch (choice) {
						case 1:
							// GitHub
							String gitHubSlug = "" + textField.getText();
							gitHubSlug = gitHubSlug.replaceAll("https://github.com/", "https://raw.githubusercontent.com/");
							gitHubSlug = gitHubSlug.replaceAll("https://www.github.com/", "https://www.raw.githubusercontent.com/");
							gitHubSlug = gitHubSlug.replaceAll("/blob/", "/");

							try {
								cachedString = getUrlContent(gitHubSlug);
								previousMalformed = "" + textField.getText();
								isMalformed = false;
							} catch (IOException ex) {
								//
								previousMalformed = "" + textField.getText();
								isMalformed = true;
							}
							break;
						case 2:
							// DiseaseCraft.com
							previousMalformed = "" + textField.getText();
							isMalformed = true;
							break;
						case 3:
							// Pastebin
							String slug = "" + textField.getText();
							slug = slug.replaceAll("http://pastebin.com/", "");
							slug = slug.replaceAll("http://www.pastebin.com/", "");

							try {
								cachedString = getUrlContent("http://pastebin.com/raw.php?i=" + slug);
								previousMalformed = "" + textField.getText();
								isMalformed = false;
							} catch (IOException ex) {
								//
								previousMalformed = "" + textField.getText();
								isMalformed = true;
							}
							break;
						case 4:
							try {
								cachedString = getUrlContent(textField.getText());
								previousMalformed = "" + textField.getText();
								isMalformed = false;
							} catch (IOException ex) {
								//
								previousMalformed = "" + textField.getText();
								isMalformed = true;
								strings.add(EnumChatFormatting.RED + "URL does not format correctly");
							}
							break;
					}
					previousButton = choice;
				} else {
					if (isMalformed)
						strings.add(EnumChatFormatting.RED + "URL does not format correctly");
					else
						strings.add("Download JSON diseases");
				}
			/*} else {
				if (textField.getText().isEmpty()) {
					strings.add(EnumChatFormatting.RED + "1 PREREQUISITE REQUIRED: ");
					strings.add(EnumChatFormatting.RED + "- URL is empty");
				}
			}*/
			drawHoveringString(strings,mouseX,mouseY);
		}
	}
}
