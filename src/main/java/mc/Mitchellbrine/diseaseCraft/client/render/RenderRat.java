package mc.Mitchellbrine.diseaseCraft.client.render;

import mc.Mitchellbrine.diseaseCraft.entity.EntityRat;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Mitchellbrine on 2015.
 */
public class RenderRat extends RenderLiving {

	public ModelSilverfish model;

	private static final ResourceLocation rat = new ResourceLocation("DiseaseCraft:textures/mob/rat.png");
	private static final ResourceLocation scaryRat = new ResourceLocation("DiseaseCraft:textures/mob/rat_scary.png");

	public static String room1People = "Lomeli12 azreth allout58 mage_kkaylium YSPilot sci4me";

	public RenderRat()
	{
		super(new ModelSilverfish(), 0.3F);
	}

	protected ResourceLocation getEntityTexture(EntityRat rat)
	{
		if (rat.getAITarget() != null) {
			return scaryRat;
		}
		return this.rat;
	}
	protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase)
	{
		return 180.0F;
	}

	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.getEntityTexture((EntityRat)par1Entity);
	}

}
