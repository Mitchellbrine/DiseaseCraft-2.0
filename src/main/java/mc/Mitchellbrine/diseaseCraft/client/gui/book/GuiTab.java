package mc.Mitchellbrine.diseaseCraft.client.gui.book;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public abstract class GuiTab extends GuiRectangle 
{
    int values, del;
    private String name;

    public GuiTab(String name, int id) 
    {
        super(-62, 10 + 19*id, 19, 18);        
        this.name = name;
        values = 0;
        del = 0;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public abstract void drawBackground(GuiBook gui, int x, int y, int page);
    public abstract void drawForeground(GuiBook gui, int x, int y, int page);
    public void mouseClick(GuiBook gui, int x, int y, int button) {}
    public void mouseMoveClick(GuiBook gui, int x, int y, int button, long timeSinceClicked) {}
    public void mouseReleased(GuiBook gui, int x, int y, int button) {}
    public abstract boolean isEnabled(GuiBook book, EntityPlayer player);

    public int getMaxPages()
    {
        return 1;
    }
    
}
