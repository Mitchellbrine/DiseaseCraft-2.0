package mc.Mitchellbrine.diseaseCraft.proxy;

import mc.Mitchellbrine.diseaseCraft.client.render.RenderRat;
import mc.Mitchellbrine.diseaseCraft.entity.EntityRat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerStuff() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRat.class, new RenderRat(Minecraft.getMinecraft().getRenderManager()));
	}
}
