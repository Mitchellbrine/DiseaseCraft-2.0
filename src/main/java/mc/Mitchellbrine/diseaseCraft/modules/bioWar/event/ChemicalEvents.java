package mc.Mitchellbrine.diseaseCraft.modules.bioWar.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mc.Mitchellbrine.diseaseCraft.entity.EntityRat;
import mc.Mitchellbrine.diseaseCraft.modules.ModuleWarfare;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import java.util.Random;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ChemicalEvents {

	@SubscribeEvent
	public void interact(EntityInteractEvent event) {
		Random random = new Random();

		if (!event.entityLiving.worldObj.isRemote) {
			if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == ModuleWarfare.chemicalExtractor && event.entityPlayer.getCurrentEquippedItem().getItemDamage() == 0) {
				if (event.target instanceof EntityRat) {
					if (random.nextInt(100) <= 5) {
						event.target.attackEntityFrom(DamageSource.causeMobDamage(event.entityPlayer), 1.0F);
						ItemStack stack = new ItemStack(ModuleWarfare.chemicalExtractor, 1, 1);
						stack.setTagCompound(new NBTTagCompound());
						stack.getTagCompound().setString("disease", "bubonicPlague");
						event.entityPlayer.setCurrentItemOrArmor(0, stack);
					}
				}
				if (event.target instanceof EntityWolf) {
					if (!((EntityWolf) event.target).isTamed()) {
						if (random.nextInt(100) <= 17) {
							event.target.attackEntityFrom(DamageSource.causeMobDamage(event.entityPlayer), 1.0F);
							ItemStack stack = new ItemStack(ModuleWarfare.chemicalExtractor, 1, 1);
							stack.setTagCompound(new NBTTagCompound());
							stack.getTagCompound().setString("disease", "rabies");
							event.entityPlayer.setCurrentItemOrArmor(0, stack);
						}
					}
				}
				if (event.target instanceof EntityPlayer || event.target instanceof EntityZombie) {
					if (random.nextInt(100) <= 20) {
						event.target.attackEntityFrom(DamageSource.causeMobDamage(event.entityPlayer), 1.0F);
						ItemStack stack = new ItemStack(ModuleWarfare.chemicalExtractor, 1, 1);
						stack.setTagCompound(new NBTTagCompound());
						stack.getTagCompound().setString("disease", "influenza");
						event.entityPlayer.setCurrentItemOrArmor(0, stack);
					}
				}
			}
		}

	}

}
