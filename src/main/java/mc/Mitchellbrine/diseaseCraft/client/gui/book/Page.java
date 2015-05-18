package mc.Mitchellbrine.diseaseCraft.client.gui.book;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class Page
{
	private static Random random = new Random();

    public static void addCraftingRecipeTextPage(GuiBook gui, int x, int y, boolean isSmall, List<String> text, List<ItemStack> items, int mouseX, int mouseY)
    {
        y+=5;
        gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + items.get(0).getDisplayName(), x + Math.abs(70 - gui.getFont().getStringWidth(items.get(0).getDisplayName())/2) - 10,  y - 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("magiciansartifice", "textures/guis/guidePageFlip.png"));
        List<String> name = new ArrayList<String>();
        if(isSmall){
            gui.drawTexturedModalRect(x, y + 10, 145, 54, 111, 46);
            gui.renderItem(items.get(0), x + 89, y + 22 + 10, 30f);
//            gui.drawRect(x, y + 10, x + 111, y + 46, 325325);
            if(items.size() > 1 && items.get(1) != null){
                gui.renderItem(items.get(1), x + 8, y + 16 + 10, 30f);
                if(x - gui.getLeft() >= x + 8) gui.drawHoverString(text, x, y);
                name.add(items.get(1).getDisplayName());
                if(mouseX >= x && mouseX <= x + 16 && mouseY >= y + 10 && mouseY <= y + 26) gui.drawHoverString(name, x - 8, y + 10);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 2 && items.get(2) != null){
                gui.renderItem(items.get(2), x + 30, y + 16 + 10, 30f);
                name.add(items.get(2).getDisplayName());
                if(mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 10 && mouseY <= y + 26) gui.drawHoverString(name, x + 15, y + 10);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 3 && items.get(3) != null){
                gui.renderItem(items.get(3), x + 8, y + 40 + 10, 30f);
                name.add(items.get(3).getDisplayName());
                if(mouseX >= x && mouseX <= x + 16 && mouseY >= y + 36 && mouseY <= y + 36 + 16) gui.drawHoverString(name, x - 8, y + 35);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 4 && items.get(4) != null){
                gui.renderItem(items.get(4), x + 30, y + 40 + 10, 30f);
                name.add(items.get(4).getDisplayName());
                if(mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 36 && mouseY <= y + 36 + 16) gui.drawHoverString(name, x + 15, y + 35);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 55 + i*12, 0);
        }
        else{
            gui.drawTexturedModalRect(x, y + 10, 145, 0, 111, 54);
            gui.renderItem(items.get(0), x + 91, y + 28 + 10, 30f);
            if(items.size() > 1 && items.get(1) != null){
                gui.renderItem(items.get(1), x + 8, y + 20, 30f);
                name.add(items.get(1).getDisplayName());
                if(mouseX >= x && mouseX <= x + 16 && mouseY >= y + 10 && mouseY <= y + 26) gui.drawHoverString(name, x + 8, y + 10);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 2 && items.get(2) != null){
                gui.renderItem(items.get(2), x + 28, y + 20, 30f);
                name.add(items.get(2).getDisplayName());
                if(mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 10 && mouseY <= y + 26) gui.drawHoverString(name, x + 28, y + 10);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 3 && items.get(3) != null){
                gui.renderItem(items.get(3), x + 45, y + 20, 30f);
                name.add(items.get(3).getDisplayName());
                if(mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 10 && mouseY <= y + 26) gui.drawHoverString(name, x + 45, y + 10);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 4 && items.get(4) != null){
                gui.renderItem(items.get(4), x + 8, y + 37, 30f);
                name.add(items.get(4).getDisplayName());
                if(mouseX >= x && mouseX <= x + 16 && mouseY >= y + 27 && mouseY <= y + 27 + 16) gui.drawHoverString(name, x + 8, y + 27);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 5 && items.get(5) != null){
                gui.renderItem(items.get(5), x + 28, y + 37, 30f);
                name.add(items.get(5).getDisplayName());
                if(mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 27 && mouseY <= y + 27 + 16) gui.drawHoverString(name, x + 28, y + 27);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 6 && items.get(6) != null){
                gui.renderItem(items.get(6), x + 45, y + 37, 30f);
                name.add(items.get(6).getDisplayName());
                if(mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 27 && mouseY <= y + 27 + 16) gui.drawHoverString(name, x + 45, y + 27);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 7 && items.get(7) != null){
                gui.renderItem(items.get(7), x + 8, y + 57, 30f);
                name.add(items.get(7).getDisplayName());
                if(mouseX >= x && mouseX <= x + 16 && mouseY >= y + 47 && mouseY <= y + 47 + 16) gui.drawHoverString(name, x + 8, y + 47);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 8 && items.get(8) != null){
                gui.renderItem(items.get(8), x + 28, y + 57, 30f);
                name.add(items.get(8).getDisplayName());
                if(mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 47 && mouseY <= y + 47 + 16) gui.drawHoverString(name, x + 28, y + 47);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if(items.size() > 9 && items.get(9) != null){
                gui.renderItem(items.get(9), x + 45, y + 57, 30f);
                name.add(items.get(9).getDisplayName());
                if(mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 47 && mouseY <= y + 47 + 16) gui.drawHoverString(name, x + 45, y + 47);
                name.removeAll(name);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 62 + i*12, 0);
            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addCraftingRecipeTextPage(GuiBook gui, int x, int y, List<String> text, ItemStack recipeStack, int mouseX, int mouseY)
    {
        boolean isSmall = false;
        IRecipe recipeI = new RecipeBookCloning();
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size();i++) {
            if (CraftingManager.getInstance().getRecipeList().get(i) != null) {
                IRecipe stackRecipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);
                if (recipeStack != null) {
                    if (stackRecipe.getRecipeOutput() == recipeStack) {
                        recipeI = stackRecipe;
                    }
                }
            }
        }

        if (recipeI.getRecipeOutput() != null) {
            List<ItemStack> items = new ArrayList<ItemStack>();
            items.add(recipeI.getRecipeOutput());
            if (recipeI instanceof ShapedOreRecipe) {
                ShapedOreRecipe recipe = (ShapedOreRecipe) recipeI;
                if (recipe.getInput().length == 2) {
                    isSmall = true;
                }

                ItemStack[] stacks = (ItemStack[]) recipe.getInput();
                for (ItemStack stack : stacks) {
                    items.add(stack.copy());
                }
            } else if (recipeI instanceof ShapedRecipes) {
                ShapedRecipes recipe = (ShapedRecipes) recipeI;
                if (recipe.recipeHeight == 2) {
                    isSmall = true;
                }

                for (ItemStack stack : recipe.recipeItems) {
                    items.add(stack.copy());
                }
            } else if (recipeI instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe recipe = (ShapelessOreRecipe) recipeI;
                if (recipe.getRecipeSize() == 2) {
                    isSmall = true;
                }

                ItemStack[] stacks = (ItemStack[]) recipe.getInput().toArray();
                for (ItemStack stack : stacks) {
                    items.add(stack.copy());
                }
            } else if (recipeI instanceof ShapelessRecipes) {
                ShapelessRecipes recipe = (ShapelessRecipes) recipeI;
                if (recipe.getRecipeSize() == 2) {
                    isSmall = true;
                }

                List<ItemStack> stacks = recipe.recipeItems;
                for (ItemStack stack : stacks) {
                    items.add(stack.copy());
                }
            }

            y += 5;
            gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + items.get(0).getDisplayName(), x + Math.abs(70 - gui.getFont().getStringWidth(items.get(0).getDisplayName()) / 2) - 10, y - 2, 0);
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("magiciansartifice", "textures/guis/guidePageFlip.png"));
            List<String> name = new ArrayList<String>();
            if (isSmall) {
                gui.drawTexturedModalRect(x, y + 10, 145, 54, 111, 46);
                gui.renderItem(items.get(0), x + 89, y + 22 + 10, 30f);
//            gui.drawRect(x, y + 10, x + 111, y + 46, 325325);
                if (items.size() > 1 && items.get(1) != null) {
                    gui.renderItem(items.get(1), x + 8, y + 16 + 10, 30f);
                    if (x - gui.getLeft() >= x + 8) gui.drawHoverString(text, x, y);
                    name.add(items.get(1).getDisplayName());
                    if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + 10 && mouseY <= y + 26)
                        gui.drawHoverString(name, x - 8, y + 10);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 2 && items.get(2) != null) {
                    gui.renderItem(items.get(2), x + 30, y + 16 + 10, 30f);
                    name.add(items.get(2).getDisplayName());
                    if (mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 10 && mouseY <= y + 26)
                        gui.drawHoverString(name, x + 15, y + 10);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 3 && items.get(3) != null) {
                    gui.renderItem(items.get(3), x + 8, y + 40 + 10, 30f);
                    name.add(items.get(3).getDisplayName());
                    if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + 36 && mouseY <= y + 36 + 16)
                        gui.drawHoverString(name, x - 8, y + 35);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 4 && items.get(4) != null) {
                    gui.renderItem(items.get(4), x + 30, y + 40 + 10, 30f);
                    name.add(items.get(4).getDisplayName());
                    if (mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 36 && mouseY <= y + 36 + 16)
                        gui.drawHoverString(name, x + 15, y + 35);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                for (int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 55 + i * 12, 0);
            } else {
                gui.drawTexturedModalRect(x, y + 10, 145, 0, 111, 54);
                gui.renderItem(items.get(0), x + 91, y + 28 + 10, 30f);
                if (items.size() > 1 && items.get(1) != null) {
                    gui.renderItem(items.get(1), x + 8, y + 20, 30f);
                    name.add(items.get(1).getDisplayName());
                    if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + 10 && mouseY <= y + 26)
                        gui.drawHoverString(name, x + 8, y + 10);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 2 && items.get(2) != null) {
                    gui.renderItem(items.get(2), x + 28, y + 20, 30f);
                    name.add(items.get(2).getDisplayName());
                    if (mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 10 && mouseY <= y + 26)
                        gui.drawHoverString(name, x + 28, y + 10);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 3 && items.get(3) != null) {
                    gui.renderItem(items.get(3), x + 45, y + 20, 30f);
                    name.add(items.get(3).getDisplayName());
                    if (mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 10 && mouseY <= y + 26)
                        gui.drawHoverString(name, x + 45, y + 10);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 4 && items.get(4) != null) {
                    gui.renderItem(items.get(4), x + 8, y + 37, 30f);
                    name.add(items.get(4).getDisplayName());
                    if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + 27 && mouseY <= y + 27 + 16)
                        gui.drawHoverString(name, x + 8, y + 27);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 5 && items.get(5) != null) {
                    gui.renderItem(items.get(5), x + 28, y + 37, 30f);
                    name.add(items.get(5).getDisplayName());
                    if (mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 27 && mouseY <= y + 27 + 16)
                        gui.drawHoverString(name, x + 28, y + 27);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 6 && items.get(6) != null) {
                    gui.renderItem(items.get(6), x + 45, y + 37, 30f);
                    name.add(items.get(6).getDisplayName());
                    if (mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 27 && mouseY <= y + 27 + 16)
                        gui.drawHoverString(name, x + 45, y + 27);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 7 && items.get(7) != null) {
                    gui.renderItem(items.get(7), x + 8, y + 57, 30f);
                    name.add(items.get(7).getDisplayName());
                    if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + 47 && mouseY <= y + 47 + 16)
                        gui.drawHoverString(name, x + 8, y + 47);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 8 && items.get(8) != null) {
                    gui.renderItem(items.get(8), x + 28, y + 57, 30f);
                    name.add(items.get(8).getDisplayName());
                    if (mouseX >= x + 20 && mouseX <= x + 16 + 20 && mouseY >= y + 47 && mouseY <= y + 47 + 16)
                        gui.drawHoverString(name, x + 28, y + 47);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                if (items.size() > 9 && items.get(9) != null) {
                    gui.renderItem(items.get(9), x + 45, y + 57, 30f);
                    name.add(items.get(9).getDisplayName());
                    if (mouseX >= x + 40 && mouseX <= x + 16 + 40 && mouseY >= y + 47 && mouseY <= y + 47 + 16)
                        gui.drawHoverString(name, x + 45, y + 47);
                    name.removeAll(name);
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                for (int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 62 + i * 12, 0);
                GL11.glColor4f(1, 1, 1, 1);
            }
        }
    }


    public static void addSmeltingRecipeTextPage(GuiBook gui, int x, int y, List<String> text, List<ItemStack> items, int mouseX, int mouseY)
    {
        ArrayList<String> name = new ArrayList<String>();
        gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + items.get(1).getDisplayName(), x + Math.abs(70 - gui.getFont().getStringWidth(items.get(0).getDisplayName())/2), y + 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("magiciansartifice", "textures/guis/guidePageFlip" + ".png"));
        gui.drawTexturedModalRect(x, y + 10, 145, 100, 111, 46);

        gui.renderItem(items.get(0), x + 13, y + 20 + 10, 50f);
        name.add(items.get(0).getDisplayName());
        if(mouseX >= x && mouseX <= x + 20 && mouseY >= y + 20 && mouseY <= y + 20 + 16) gui.drawHoverString(name, x, y + 20);
        name.removeAll(name);
        GL11.glDisable(GL11.GL_LIGHTING);

        gui.renderItem(items.get(1), x + 77, y + 28 + 10, 50f);

        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 60 + i*12, 0);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void addImageTextPage(GuiBook gui, int x, int y, ItemStack item, List<String> text, float size)
    {
        y+=5;
        gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + item.getDisplayName(), x + Math.abs(70 - gui.getFont().getStringWidth(item.getDisplayName())/2) - 10,  y + 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        gui.renderItem(item, x + 13, y + 18, size);
        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 30 + i*12, 0);
    }

    public static void addImageTextPage(GuiBook gui, int x, int y, ItemStack item, List<String> text, float size, int txtX, int txtY, boolean showName, int imgX, int imgY)
    {
        y+=5;
        if(showName) gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + item.getDisplayName(), x + Math.abs(70 - gui.getFont().getStringWidth(item.getDisplayName())/2) - 10,  y + 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        gui.renderItem(item, x + 13 + imgX, y + 18 + imgY, size);
        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x + txtX, y + 30 + txtY + i*12, 0);
    }

    public static void addImageTextPage(GuiBook gui, int x, int y, Entity entity, List<String> text, float size)
    {
        y+=5;
        gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + entity.getCommandSenderName(), x + Math.abs(70 - gui.getFont().getStringWidth(entity.getCommandSenderName())/2) - 10,  y + 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        gui.renderEntity(entity, x + 13, y + 18, size);
        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 30 + i*12, 0);
    }

    public static void addImageTextPage(GuiBook gui, int x, int y, Entity entity, List<String> text, float size, int txtX, int txtY, boolean showName, int imgX, int imgY)
    {
        y+=5;
        if(showName) gui.getFont().drawString(EnumChatFormatting.DARK_BLUE + "\u00a7n" + entity.getCommandSenderName(), x + Math.abs(70 - gui.getFont().getStringWidth(entity.getCommandSenderName())/2) - 10,  y + 2, 0);
        GL11.glColor4f(1, 1, 1, 1);
        gui.renderEntity(entity, x + 13 + imgX, y + 18 + imgY, size);
        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x + txtX, y + 30 + txtY + i*12, 0);
    }

    public static void addTextPage(GuiBook gui, int x, int y, List<String> text)
    {
        y-=25;
        for(int i = 0; i < text.size(); i++) gui.getFont().drawString(text.get(i), x, y + 30 + i*12, 0);
        GL11.glColor4f(1, 1, 1, 1);
    }
}
