package mc.Mitchellbrine.diseaseCraft.client.gui.book;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GuiIntroTab extends GuiTab {

	public GuiIntroTab(int id)
	{
		super("Introduction",id);
	}

	public ItemStack getIcon()
	{
		return new ItemStack(Items.writable_book);
	}

	@Override
	public void drawBackground(GuiBook gui, int x, int y, int page)
	{
		ArrayList<String> text = new ArrayList<String>();
		int xPos = (page%2==0)?107:-35;
		switch(page)
		{
			case 1:
				text.add("The world of plague ");
				text.add("lies in the pit of man's ");
				text.add("fears and the summit ");
				text.add("of his mortal being. It");
				text.add("fascinates but intrigues ");
				text.add("him. We wish we could ");
				text.add("warn him, but he goes ");
				text.add("on, the irony in tact. ");
				text.add("Disease kills man. ");
				text.add("But he wishes to know");
				text.add("more.");
				Page.addTextPage(gui, gui.getLeft() + xPos, gui.getTop(), text);
				break;
			default:
				break;
		}
	}

	public int getMaxPages()
	{
		return 2;
	}

	public void mouseClick(GuiBook gui, int x, int y, int button)
	{
		//A way to add links. Very easy
		//        if(gui.page == 13 && x >= gui.getLeft() + 138 && x <= gui.getLeft() + 168 && y >= gui.getTop() + 98 && y <= gui.getTop() + 108)
		//        {
		//            try
		//            {
		//                Desktop.getDesktop().browse(new URL("http://www.google.com/").toURI());
		//            }
		//            catch (Exception e) {}
		//        }
	}

	@Override
	public boolean isEnabled(GuiBook book, EntityPlayer player) {
		return true;
	}

	@Override
	public void drawForeground(GuiBook gui, int x, int y, int page)
	{
	}

}
