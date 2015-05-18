package mc.Mitchellbrine.diseaseCraft.client.gui;

import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.client.gui.book.GuiBook;
import mc.Mitchellbrine.diseaseCraft.client.gui.book.GuiDiseaseTab;
import mc.Mitchellbrine.diseaseCraft.client.gui.book.GuiIntroTab;
import mc.Mitchellbrine.diseaseCraft.client.gui.book.GuiTab;
import mc.Mitchellbrine.diseaseCraft.config.ConfigRegistry;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GuiJournal extends GuiBook {

	private boolean isUser = false;

	public GuiJournal(EntityPlayer player) {
		super(player);
	}

	public GuiJournal(EntityPlayer player, boolean isUser) {
		super(player);
		this.isUser = isUser;
	}

	@Override
	public void initGui() {
		super.initGui();
		super.tabs.clear();
		addTab(new GuiIntroTab(getTabAmount()));
		if (!isUser) {
			for (Disease disease : Diseases.diseases) {
				if (disease.isJoke() || !disease.isVanilla())
					continue;
				GuiTab tab = new GuiDiseaseTab(getTabAmount(), disease);
				if (tab.isEnabled(this, super.player)) {
					addTab(tab);
				}
			}
		} else {
			for (String id : ConfigRegistry.userDiseases) {
				Disease disease = DiseaseHelper.getDiseaseInstance(id);
				if (disease != null) {
					if ((disease.isJoke() && ConfigRegistry.journalLevel == 0) || (!disease.isJoke() && ConfigRegistry.journalLevel == 1))
						continue;
					GuiTab tab = new GuiDiseaseTab(getTabAmount(),disease);
					if (tab.isEnabled(this,super.player)) {
						addTab(tab);
					}
				} else {
					ConfigRegistry.logger.error(String.format("The specified disease %s does not exist, skipping...",id));
				}
			}
		}
	}
}
