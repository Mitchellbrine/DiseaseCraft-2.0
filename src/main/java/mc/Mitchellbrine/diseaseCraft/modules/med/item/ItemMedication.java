package mc.Mitchellbrine.diseaseCraft.modules.med.item;

import cpw.mods.fml.common.registry.GameRegistry;
import mc.Mitchellbrine.diseaseCraft.modules.Medicine;
import mc.Mitchellbrine.diseaseCraft.modules.med.recipe.MedicationRecipes;
import mc.Mitchellbrine.diseaseCraft.modules.med.util.MedUtils;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ItemMedication extends Item {

	public ItemMedication() {
		this.setCreativeTab(Medicine.tab);
		this.setUnlocalizedName("medication");
		this.setTextureName(References.MODID + ":medication");
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.eat;
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		MedUtils.applyEffect(player,stack.getTagCompound());
		stack = new ItemStack(Items.glass_bottle);
		return stack;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean par4) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("medName")) {
				lore.add("Medication Name: " + stack.getTagCompound().getString("medName"));
			}
			if (stack.getTagCompound().hasKey("diseaseHealName")) {
				lore.add("Treats: " + stack.getTagCompound().getString("diseaseHealName"));
			}
			if (stack.getTagCompound().hasKey("dangerous") && stack.getTagCompound().getBoolean("dangerous")) {
				lore.add("");
				lore.add(EnumChatFormatting.RED + "WARNING: NOT ENOUGH SUPPRESSION");
			}
			if (stack.getTagCompound().hasKey("tooSuppressed") && stack.getTagCompound().getBoolean("tooSuppressed")) {
				lore.add("");
				lore.add(EnumChatFormatting.RED + "WARNING: TOO SUPPRESSED TO FUNCTION");
			}
		}
		/*
		lore.add(EnumChatFormatting.YELLOW + "- - -");
		lore.add(EnumChatFormatting.YELLOW + "DEPRECATED: This item will be removed or changed ");
		lore.add(EnumChatFormatting.YELLOW + "in future versions and will not work ");
		lore.add(EnumChatFormatting.YELLOW + "as it does currently if so!");
		*/
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List items) {
		items.addAll(MedicationRecipes.medicationStacks);
	}
}
