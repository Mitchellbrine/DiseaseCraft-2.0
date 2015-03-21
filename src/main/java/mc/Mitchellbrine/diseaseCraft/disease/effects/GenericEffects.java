package mc.Mitchellbrine.diseaseCraft.disease.effects;

import mc.Mitchellbrine.diseaseCraft.api.Disease;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;

import java.util.Random;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GenericEffects {

	public static Random rand = new Random();

	public static void applyEffects(EntityPlayer player, Disease disease) {
		rand.setSeed(player.worldObj.getTotalWorldTime());
		for (int effect : disease.effects) {
			if (effect > 0) {
				player.addPotionEffect(new PotionEffect(effect, 100, 0, true, false));
			} else {
				switch (effect) {
					case -1:
						jitter(player);
						break;
					case -2:
						dropItem(player);
						break;
					case -3:
						hydrophobia(player);
						break;
				}
			}
		}
	}

	public static void jitter(EntityPlayer player) {

		if (rand.nextInt(750) == 0) {
			int direction = rand.nextInt(4);

			if (direction == 0) {
				player.moveEntity(1.0F, 0.0F, 0.0F);
			} else if (direction == 1) {
				player.moveEntity(0.0F, 0.0F, 1.0F);
			} else if (direction == 2) {
				player.moveEntity(-1.0F, 0.0F, 0.0F);

			} else if (direction == 3) {
				player.moveEntity(0.0F, 0.0F, -1.0F);
			} else if (direction == 4) {
				player.moveEntity(0.0F, 1.0F, 0.0F);
			}
		}
	}

	public static void dropItem(EntityPlayer player) {
		if (rand.nextInt(100000) == 0) {
			player.dropOneItem(rand.nextInt(100) > 50);
		}
	}

	public static void hydrophobia(EntityPlayer player) {
		BlockPos bodyUp = new BlockPos(player.posX,player.posY,player.posZ);
		BlockPos bodyDown = new BlockPos(player.posX,player.posY - 1,player.posZ);
		if (player.worldObj.getBlockState(bodyUp).getBlock().getMaterial() == Material.water || player.worldObj.getBlockState(bodyDown).getBlock().getMaterial() == Material.water) {
			if (player.worldObj.getTotalWorldTime() % 20 == 0) {
				player.attackEntityFrom(DamageSource.drown,1.0F);
			}
		}
	}

}
