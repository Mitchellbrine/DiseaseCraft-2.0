package mc.Mitchellbrine.diseaseCraft.client.gui.book;

import com.google.gson.JsonElement;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseTabEvent;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.modules.ModuleWarfare;
import mc.Mitchellbrine.diseaseCraft.utils.ClassHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GuiDiseaseTab extends GuiTab {

	private Disease disease;

	public GuiDiseaseTab(int id, Disease disease)
	{
		super(StatCollector.translateToLocal(disease.getUnlocalizedName()), id);
		this.disease = disease;
	}

	public ItemStack getIcon()
	{
		if (ClassHelper.getModule("bioWarfare") != null)
			return new ItemStack(ModuleWarfare.chemicalExtractor,1,1);
		return new ItemStack(Items.written_book);
	}

	@Override
	public void drawBackground(GuiBook gui, int x, int y, int page)
	{
		List<String> text = new ArrayList<String>();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		int xPos = (page%2==0)?107:-35;
				// 1 text.add("To Whom it May Concern,");
				// 2 text.add("the spells are what ");
				// 3 text.add("separate magician from ");
				// 4 text.add("mortal. Using spells, we");
				// 5 text.add("can achieve great ");
				// 6 text.add("feats. I might not be ");
				// 7 text.add("able to accomplish this ");
				// 8 text.add("feat, but I know you ");
				// 9 text.add("can do this and protect ");
				// 10 text.add("the mortals!");
				// 11 text.add("");
				// 12 text.add("-Merlin");
		if (page == 1) {
			text = Arrays.asList(disease.getLore());
			Page.addTextPage(gui, gui.getLeft() + xPos, gui.getTop(), text);
		} else if (page == 2) {
			text.add(StatCollector.translateToLocal(disease.getUnlocalizedName()));
			for (int i = 0; i < 1; i++)
				text.add("");
			text.add("Symptoms:");
			for (Integer effect : disease.getEffects()) {
				if (effect >= 0) {
					text.add("- " + StatCollector.translateToLocal(Potion.potionTypes[effect].getName()));
				} else {
					if (Diseases.modesAndNames.get(Diseases.modesAndMethods.get(effect)) != null) {
						if (!Diseases.modesAndNames.get(Diseases.modesAndMethods.get(effect)).equalsIgnoreCase("death")) {
							text.add("- " + StatCollector.translateToLocal("symptom." + Diseases.modesAndNames.get(Diseases.modesAndMethods.get(effect))));
						}
					}
				}
			}
			if (disease.getDeathRate() > 0) {
				text.add(disease.getDeathRate() + "% chance of death");
			}

			Page.addTextPage(gui, gui.getLeft() + xPos, gui.getTop(), text);
		} else if (page > 2) {
			int index = getMaxPages() - (page);
			if (index < 0 || disease.getWaysToContract().get(index) == null) {
				return;
			}
			Entity entity = null;
			ItemStack stack = null;
			boolean showName = true;
			String type = disease.getWaysToContract().get(index);
			if (type.equalsIgnoreCase("mob") || type.equalsIgnoreCase("mobAttack")) {
				try {
					entity = (Entity) ((Class) EntityList.stringToClassMapping.get(((JsonElement)disease.getParameters(type)[0]).getAsString())).getConstructor(World.class).newInstance(gui.world);
				} catch (Exception e) {
					System.out.println(((JsonElement)disease.getParameters(type)[0]).getAsString());
					e.printStackTrace();
				}
				if (type.equalsIgnoreCase("mobAttack")) {
					text.add("From: Mob Attack");
				} else {
					text.add("From: Mobs");
				}
				text.add("");
				text.add(((100.0 - ((JsonElement) disease.getParameters(type)[1]).getAsInt() / 10000) / 100) + "% chance");
			} else if (type.equalsIgnoreCase("eaten")) {
				stack = new ItemStack(Item.getItemById(((JsonElement)disease.getParameters(type)[0]).getAsInt()),1,((JsonElement)disease.getParameters(type)[1]).getAsInt());
				text.add("From: Eating");
				text.add("");
				text.add("Food: " + StatCollector.translateToLocal(Item.getItemById(((JsonElement) disease.getParameters(type)[0]).getAsInt()).getUnlocalizedName() + ".name"));
				text.add(((100.0 - ((JsonElement)disease.getParameters(type)[2]).getAsInt() / 10000) / 100.0) + "% chance");
			} else if (type.equalsIgnoreCase("temp")) {
				text.add("From: Temperature");
				text.add("");
				text.add("Temp: " + (100.0 * ((JsonElement)disease.getParameters(type)[0]).getAsDouble()) + " degrees");
				text.add(((100.0 - ((JsonElement)disease.getParameters(type)[1]).getAsInt() / 10000) / 100) + "% chance");
				if ((100.0 * ((JsonElement)disease.getParameters(type)[0]).getAsDouble()) < 50) {
					stack = new ItemStack(Items.snowball);
				} else {
					stack = new ItemStack(Item.getItemFromBlock(Blocks.fire));
				}
				stack.setStackDisplayName("Temperature");
			} else if (type.equalsIgnoreCase("stat")) {
				stack = new ItemStack(Items.writable_book);
				stack.setStackDisplayName(StatCollector.translateToLocal(((JsonElement)disease.getParameters(type)[0]).getAsString()));
				text.add("From: Statistic");
				text.add("At Point: " + ((JsonElement)disease.getParameters(type)[1]).getAsInt());
				text.add("");
				text.add(((100.0 - ((JsonElement)disease.getParameters(type)[3]).getAsInt() / 10000) / 100.0) + "% chance");
			} else {
				DiseaseTabEvent event = new DiseaseTabEvent(gui.world,disease,this,type);
				if (FMLCommonHandler.instance().bus().post(event)) {
					return;
				}
			}
			if (entity == null && stack == null) {
				Page.addTextPage(gui, gui.getLeft() + xPos, gui.getTop(),text);
			} else {
				if (entity != null) {
					Page.addImageTextPage(gui, gui.getLeft() + xPos, gui.getTop(),entity,text,25.0F,0,0,showName,0,0);
				} else {
					Page.addImageTextPage(gui, gui.getLeft() + xPos, gui.getTop(),stack,text,30.0F,0,0,showName,0,0);
				}
			}
		}
			/*
			case 2:
				Page.addTextPage(gui, gui.getLeft() + xPos, gui.getTop(), text);
				break;
				*/
	}

	public int getMaxPages()
	{
		return 2 + disease.getWaysToContract().size();
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
		//System.out.println(disease.getUnlocalizedName().replaceAll(".name",".complete") + " | " + player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10) + " | " + player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).hasKey(disease.getUnlocalizedName().replaceAll(".name", ".complete")) + " | " + player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean(disease.getUnlocalizedName().replaceAll(".name",".complete")));
		return player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).hasKey(disease.getUnlocalizedName().replaceAll(".name",".complete")) && player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean(disease.getUnlocalizedName().replaceAll(".name",".complete"));
	}

	@Override
	public void drawForeground(GuiBook gui, int x, int y, int page)
	{
	}

}
