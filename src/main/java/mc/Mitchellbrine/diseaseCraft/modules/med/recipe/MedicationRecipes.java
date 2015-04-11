package mc.Mitchellbrine.diseaseCraft.modules.med.recipe;

import com.sun.istack.internal.NotNull;
import cpw.mods.fml.common.registry.GameRegistry;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.modules.Medicine;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MedicationRecipes {

	public static Map<ItemStack, String> diseaseRemoval = new HashMap<ItemStack, String>();
	public static Map<ItemStack, String> medicineType = new HashMap<ItemStack, String>();
	public static Map<ItemStack, Integer> suppressantValues = new HashMap<ItemStack, Integer>();
	public static List<ItemStack> medicationStacks = new ArrayList<ItemStack>();

	public static void init() {
		for (ItemStack stack : diseaseRemoval.keySet()) {
			ItemStack output = new ItemStack(Medicine.medication,1);
			output.setTagCompound(new NBTTagCompound());
			output.getTagCompound().setString("medName",medicineType.get(stack));
			output.getTagCompound().setString("diseaseHeal",diseaseRemoval.get(stack));
			output.getTagCompound().setString("diseaseHealName", StatCollector.translateToLocal(Diseases.getDiseaseName(diseaseRemoval.get(stack))));
			for (int i = 1; i <= 4;i++) {
				ItemStack newOutput = output.copy();
				newOutput.setItemDamage(i);
				if (suppressantValues.get(stack) > newOutput.getItemDamage()) {
					newOutput.getTagCompound().setBoolean("dangerous", true);
					newOutput.getTagCompound().setBoolean("tooSuppressed",false);
				} else if (suppressantValues.get(stack) < newOutput.getItemDamage()){
					newOutput.getTagCompound().setBoolean("dangerous", false);
					newOutput.getTagCompound().setBoolean("tooSuppressed",true);
				} else {
					newOutput.getTagCompound().setBoolean("dangerous", false);
					newOutput.getTagCompound().setBoolean("tooSuppressed",false);
				}
				//System.out.println("Is Dangerous for " + newOutput.getItemDamage() + ": " + newOutput.getTagCompound().getBoolean("dangerous"));
				medicationStacks.add(newOutput);
				switch (i) {
					case 1:
						GameRegistry.addRecipe(newOutput, " i ", " B ", " s ", 'i', stack, 's', Items.sugar, 'B', Items.glass_bottle);
						break;
					case 2:
						GameRegistry.addRecipe(newOutput, " i ", "sBs", 'i', stack, 's', Items.sugar, 'B', Items.glass_bottle);
						break;
					case 3:
						GameRegistry.addRecipe(newOutput, " i ", "sBs", " s ", 'i', stack, 's', Items.sugar, 'B', Items.glass_bottle);
						break;
					case 4:
						GameRegistry.addRecipe(newOutput, " i ", "sBs", "s s", 'i', stack, 's', Items.sugar, 'B', Items.glass_bottle);
						break;
				}
			}
		}
	}

	public static void addMedicationType(ItemStack stack, String curesDisease, String medicineName, int suppresentsRequired) {
		if (suppresentsRequired > 4) {
			suppresentsRequired = 4;
		}

		if (suppresentsRequired <= 0) {
			suppresentsRequired = 1;
		}

		if (curesDisease.isEmpty() || medicineName.isEmpty()) {
			Medicine.logger.error("The required string fields were empty :(");
			return;
		}

		diseaseRemoval.put(stack,curesDisease);
		medicineType.put(stack,medicineName);
		suppressantValues.put(stack,suppresentsRequired);
	}

}
