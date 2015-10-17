package mc.Mitchellbrine.diseaseCraft.disease.effects;

import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mitchellbrine on 2015.
 */
public class GenericEffects {

	public static Random rand = new Random();

	public static DamageSource illness = new DamageSource("sickness").setDamageBypassesArmor().setDifficultyScaled();

	private static GenericEffects instance;

	public static GenericEffects instance() {
		if (instance == null) {
			instance = new GenericEffects();
		}
		return instance;
	}

	public static void applyEffects(EntityLivingBase player, Disease disease) {
		rand.setSeed(player.worldObj.getTotalWorldTime());
		DiseaseEvent.DiseaseEffectEvent event = new DiseaseEvent.DiseaseEffectEvent(disease, player);
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			for (int effect : disease.effects) {
				if (effect > 0) {
					player.addPotionEffect(new PotionEffect(effect, 120, 0, true));
					if (Potion.potionTypes[effect] == Potion.digSlowdown) {
						player.addPotionEffect(new PotionEffect(effect,120,3,true));
					}
				} else {
					if (Diseases.acceptableModes.contains(effect)) {
						try {
							Diseases.modesAndMethods.get(effect).setAccessible(true);
							Diseases.modesAndMethods.get(effect).invoke(instance(), player, disease);
						} catch (Exception ex) {
							DiseaseCraft.logger.error("Exception was caught while processing effects of mode id " + effect, ex);
						}
					}
				}
			}
		}
	}

	public static void jitter(EntityLivingBase player, Disease disease) {
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

	public static void dropItem(EntityLivingBase player, Disease disease) {
		if (rand.nextInt(100000) == 0) {
			if (player instanceof EntityPlayer) {
				((EntityPlayer)player).dropOneItem(rand.nextInt(100) > 50);
			}
		}
	}

	public static void hydrophobia(EntityLivingBase player, Disease disease) {
		int posX = MathHelper.floor_double(player.posX);
		int posY = MathHelper.floor_double(player.posY);
		int posZ = MathHelper.floor_double(player.posZ);

		if (player.worldObj.getBlock(posX, posY, posZ).getMaterial() == Material.water || player.worldObj.getBlock(posX,posY-1,posZ).getMaterial() == Material.water) {
			if (player.worldObj.getTotalWorldTime() % 20 == 0) {
				player.attackEntityFrom(DamageSource.drown,1.0F);
			}
		}
	}

	public static void death(EntityLivingBase player, Disease disease) {
		if (player.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name", "")) && player.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name", "")) == 1) {
			if (rand.nextInt(100) <= disease.getDeathRate()) {
				player.attackEntityFrom(illness, 1000000F);
			}
		}
	}

	public static void coughing(EntityLivingBase player, Disease disease) {
		if (player.worldObj.getTotalWorldTime() % 160 == 0) {
			player.worldObj.playSoundAtEntity(player,References.MODID.toLowerCase() + ":cough",MathHelper.getRandomIntegerInRange(rand,3,7),1F);
			player.attackEntityFrom(illness, 2F);
		}
	}

	public static void sneezing(final EntityLivingBase player, Disease disease) {
		if (player instanceof EntityPlayer && rand.nextInt(500) == 0) {
			AxisAlignedBB bounds = ((EntityPlayer) player).boundingBox.expand(2, 0, 2);
			List<Entity> contagions = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, bounds);

			player.worldObj.playSoundAtEntity(player, References.MODID.toLowerCase() + ":sneeze", MathHelper.getRandomIntegerInRange(rand,5,10), 0F);

			System.out.println("Sneeze!");

			DiseaseCraft.scheduler.schedule(new Runnable() {
				@Override
				public void run() {
					for (int xx = (int)((EntityPlayer) player).posX - 2;xx <= (int)((EntityPlayer) player).posX + 2;xx++) {
						for (int zz = (int)((EntityPlayer) player).posZ - 2;zz <= (int)((EntityPlayer) player).posZ + 2;zz++) {
							((EntityPlayer) player).worldObj.spawnParticle("slime",xx,((EntityPlayer) player).posY + 0.5,zz,0,.5,0);
						}
					}
				}
			}, 500, TimeUnit.MILLISECONDS);



			for (Entity entity : contagions) {
				if (entity instanceof EntityLivingBase) {
					DiseaseHelper.addDisease((EntityLivingBase) entity, disease);
				}
			}
		}
	}

}
