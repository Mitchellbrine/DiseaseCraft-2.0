package mc.Mitchellbrine.diseaseCraft.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import mc.Mitchellbrine.diseaseCraft.containers.GuiBookContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == IDS.JOURNAL || ID == IDS.USER_JOURNAL) return new GuiBookContainer();
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == IDS.JOURNAL) return new GuiJournal(player);
		if (ID == IDS.USER_JOURNAL) return new GuiJournal(player,true);
		return null;
	}

	public static class IDS {
		public static final int JOURNAL = 0;
		public static final int USER_JOURNAL = 1;
	}

	public static void openGui(int id, EntityPlayer player) {
		System.out.println(id);
		switch (id) {
			case IDS.JOURNAL:
				Minecraft.getMinecraft().displayGuiScreen(new GuiJournal(player));
				break;
		}
	}

}
