package mc.Mitchellbrine.diseaseCraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ItemRegistry {

	public static List<Item> items = new ArrayList<Item>();

	public static Item medicalJournal;
	public static Item userJournal;

	public static void init() {
		medicalJournal = new MedicalJournal(false);
		userJournal = new MedicalJournal(true);

		for (Item item : items) {
			GameRegistry.registerItem(item,item.getUnlocalizedName().substring(5));
		}

	}

}
