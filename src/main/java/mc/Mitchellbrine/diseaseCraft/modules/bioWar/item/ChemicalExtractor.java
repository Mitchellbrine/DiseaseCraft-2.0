package mc.Mitchellbrine.diseaseCraft.modules.bioWar.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.modules.ModuleWarfare;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ChemicalExtractor extends Item {

	private IIcon[] icon = new IIcon[2];

	public ChemicalExtractor() {
		super();
		setCreativeTab(ModuleWarfare.tab);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setTextureName("syringe");
		GameRegistry.registerItem(this,"chemicalExtractor");
		GameRegistry.addRecipe(new ItemStack(this,1,0),"GWG","G G"," G ",'G',Blocks.glass_pane,'W',Blocks.log);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (stack.getItemDamage() == 1) {
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("disease") && entity instanceof EntityLivingBase) {
				String id = stack.getTagCompound().getString("disease");
				DiseaseHelper.addDisease((EntityLivingBase)entity,DiseaseHelper.getDiseaseInstance(id));
				stack.setTagCompound(new NBTTagCompound());
				stack.setItemDamage(0);
				return true;
			}
		}
		return false;
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean par4)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("disease") && DiseaseHelper.diseaseExists(stack.getTagCompound().getString("disease"))) {
			lore.add("Disease: " + StatCollector.translateToLocal(Diseases.getDiseaseName(stack.getTagCompound().getString("disease"))));
		}
	}

	public boolean canItemEditBlocks()
	{
		return false;
	}

	@SuppressWarnings("unchecked")
	public void getSubItems(Item item, CreativeTabs tab, List l) {
		l.add(new ItemStack(this, 1, 0));
		for (Disease disease : Diseases.diseases) {
			ItemStack stack = new ItemStack(this,1,1);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("disease",disease.getId());
			l.add(stack);
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (meta == 0) {
			return icon[meta];
		} else {
			return icon[1];
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ri) {
		icon[0] = ri.registerIcon("DiseaseCraft:syringe");
		icon[1] = ri.registerIcon("DiseaseCraft:syringeFull");
	}


	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 64;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
		if (itemstack.getItemDamage() > 0) {
			if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("disease") && DiseaseHelper.diseaseExists(itemstack.getTagCompound().getString("disease"))) {
				DiseaseHelper.addDisease(player,DiseaseHelper.getDiseaseInstance(itemstack.getTagCompound().getString("disease")));
			}
		}
		return itemstack;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par1ItemStack.getItemDamage() > 0) {
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}
		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (itemStack.getItemDamage() == 0) {
			if (world.getBlock(x,y,z) == Blocks.leaves2) {
				if (world.getBiomeGenForCoords(x,z).temperature > 1.0F) {
					if (itemRand.nextInt(100) < 10) {
						itemStack.getTagCompound().setString("disease","malaria");
					}
				}
			}

			if (world.getBlock(x,y,z) == Blocks.grass) {
				if (world.getBiomeGenForCoords(x,z).temperature > 1.0F) {
					if (itemRand.nextInt(100) < 8) {
						itemStack.getTagCompound().setString("disease","yellowFever");
					}
				}
			}
		}
		return true;
	}

}
