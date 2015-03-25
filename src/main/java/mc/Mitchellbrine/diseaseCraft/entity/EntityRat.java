package mc.Mitchellbrine.diseaseCraft.entity;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/**
 * Created by Mitchellbrine on 2015.
 */
public class EntityRat extends EntityMob {

	public EntityRat(World world) {
		super(world);
		this.setSize(0.4F, 0.3F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.0F);
	}

	public float getEyeHeight()
	{
		return 0.1F;
	}


	protected boolean canTriggerWalking()
	{
		return false;
	}

	protected String getLivingSound()
	{
		return "mob.silverfish.say";
	}

	protected String getHurtSound()
	{
		return "mob.silverfish.hit";
	}

	protected String getDeathSound()
	{
		return "mob.silverfish.kill";
	}

	public void onUpdate()
	{
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	protected Item getDropItem()
	{
		return null;
	}

	protected boolean isValidLightLevel()
	{
		return true;
	}

	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		}
		else
		{
			return false;
		}
	}

	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}


}
