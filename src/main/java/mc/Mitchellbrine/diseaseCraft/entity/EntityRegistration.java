package mc.Mitchellbrine.diseaseCraft.entity;

import mc.Mitchellbrine.diseaseCraft.DiseaseCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by Mitchellbrine on 2015.
 */
public class EntityRegistration {

	private static int START_ID = 500;

	public static void init() {
		registerEntity(new EntityRat(null),"rat",7237230,3158064);
	}

	@SuppressWarnings("unchecked")
	private static void registerEntity(Entity entity, String name) {
		int id = getUniqueId();
		EntityRegistry.registerModEntity(entity.getClass(),name,id, DiseaseCraft.instance,80,3,true);
		EntityList.idToClassMapping.put(id,entity.getClass());
		EntityList.entityEggs.put(id,new EntityList.EntityEggInfo(id,0xFFFFFF,0x111111));
	}

	@SuppressWarnings("unchecked")
	private static void registerEntity(Entity entity, String name, int color1, int color2) {
		int id = getUniqueId();
		EntityRegistry.registerModEntity(entity.getClass(),name,id, DiseaseCraft.instance,80,3,true);
		EntityList.idToClassMapping.put(id,entity.getClass());
		EntityList.entityEggs.put(id,new EntityList.EntityEggInfo(id,color1,color2));
	}

	private static int getUniqueId() {
		do
		{
			++START_ID;
		}
		while (EntityList.getStringFromID(START_ID) != null);

		return START_ID;
	}

}
