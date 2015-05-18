package mc.Mitchellbrine.diseaseCraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.client.gui.GuiHandler;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MedicalJournal extends Item {

	private boolean isUser;

	public MedicalJournal(boolean isUser) {
		super();
		this.isUser = isUser;
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabBrewing);
		if (isUser) {
			this.setUnlocalizedName("userMedicalJournal");
		} else {
			this.setUnlocalizedName("medicalJournal");
		}
		this.setTextureName(References.MODID + ":medicalJournal");
		ItemRegistry.items.add(this);
		if (!isUser) {
			GameRegistry.addRecipe(new ItemStack(this), "RWb", "FBP", "fGp", 'b', Items.bone, 'B', Items.book, 'W', Items.water_bucket,  'P', Items.porkchop, 'G', new ItemStack(Blocks.tallgrass, 1, 1), 'p', Items.diamond_pickaxe, 'F', Items.fire_charge, 'f', Items.rotten_flesh, 'R', Items.redstone);
		} else {
			GameRegistry.addShapelessRecipe(new ItemStack(this), ItemRegistry.medicalJournal,Items.writable_book);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			if (isUser) {
				player.openGui(DiseaseCraft.instance, GuiHandler.IDS.USER_JOURNAL, world, x, y, z);
			} else {
				player.openGui(DiseaseCraft.instance, GuiHandler.IDS.JOURNAL, world, x, y, z);
			}
		}
		return false;
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		if (isUser) {
			p_77624_3_.add("User-Defined Diseases");
		}
	}
}
