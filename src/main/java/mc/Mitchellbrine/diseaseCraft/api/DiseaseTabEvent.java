package mc.Mitchellbrine.diseaseCraft.api;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mc.Mitchellbrine.diseaseCraft.client.gui.book.GuiDiseaseTab;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Mitchellbrine on 2015.
 */
@SideOnly(Side.CLIENT)
@Cancelable
public class DiseaseTabEvent extends Event {

	// These are for various usage
	public GuiDiseaseTab tab;
	public Disease disease;
	public World world;

	// This is the basic identifier for your type
	public String contractingType;

	// This is what shows up in the page
	private ArrayList<String> text;

	// These start out "null" and if you assign and don't trigger final. Pointless if you trigger isFinal()
	public ItemStack stack;
	public Entity entity;

	public DiseaseTabEvent(World world, Disease disease, GuiDiseaseTab dTab, String type) {
		tab = dTab;
		this.world = world;
		contractingType = type;
		this.disease = disease;
	}

	public void setText(ArrayList<String> texts) {
		text = texts;
	}

	public void addText(String newText) {
		if (text == null) {
			text = new ArrayList<String>();
		}
		text.add(newText);
	}

	public ArrayList<String> getText() {
		return text;
	}
}
