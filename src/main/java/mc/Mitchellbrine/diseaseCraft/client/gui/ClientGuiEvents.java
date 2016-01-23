package mc.Mitchellbrine.diseaseCraft.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ClientGuiEvents {

	@SubscribeEvent
	public void menuOpen(GuiScreenEvent.InitGuiEvent.Post event) {
		/*if (event.gui instanceof GuiMainMenu) {
			event.buttonList.add(new GuiButton(101,event.gui.width - 21,0,20,20,"DC"));
		}*/
	}

	@SubscribeEvent
	public void menuButton(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.gui instanceof GuiMainMenu) {
			if (event.button.id == 101) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiDiseaseMenu((GuiMainMenu)event.gui));
			}
		}
	}

}
