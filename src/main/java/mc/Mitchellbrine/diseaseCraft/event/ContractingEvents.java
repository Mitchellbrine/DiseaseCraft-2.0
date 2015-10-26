package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.config.ConfigRegistry;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
import mc.Mitchellbrine.diseaseCraft.json.positions.PositionJSON;
import mc.Mitchellbrine.diseaseCraft.network.NBTPacket;
import mc.Mitchellbrine.diseaseCraft.network.PacketHandler;
import mc.Mitchellbrine.diseaseCraft.utils.BlockPos;
import mc.Mitchellbrine.diseaseCraft.utils.StatHelper;
import mc.Mitchellbrine.diseaseCraft.utils.StringUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ContractingEvents {

	public static Map<World,List<BlockPos>> diseasedCrops;

	static {
		diseasedCrops = new HashMap<World, List<BlockPos>>();
	}

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			for (Disease disease : Diseases.diseases) {
				for (int i = 1; i < disease.getParameters("mobAttack").length;i++) {
					if (event.source != null && event.source.getEntity() != null && EntityList.classToStringMapping != null && disease.getWaysToContract() != null && EntityList.getEntityString(event.source.getEntity()) != null && disease.getParameters("mobAttack") != null && disease.getWaysToContract().contains("mobAttack") && ((JsonPrimitive) disease.getParameters("mobAttack")[i]).getAsString().replaceAll("\"", "").equalsIgnoreCase(EntityList.getEntityString(event.source.getEntity()))) {
						GenericEffects.rand.setSeed(event.entityLiving.worldObj.getTotalWorldTime());
						if (GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive) disease.getParameters("mobAttack")[0]).getAsInt()) {
							DiseaseHelper.addDisease(event.entityLiving, disease);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void mobAroundEvent(LivingEvent.LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			for (Disease disease : Diseases.diseases) {
				if (EntityList.classToStringMapping != null && disease.getWaysToContract() != null && disease.getParameters("mob") != null && disease.getWaysToContract().contains("mob")) {
					int distance = ((JsonPrimitive) disease.getParameters("mob")[0]).getAsInt();
					for (Entity entity : (List<Entity>) event.entityLiving.worldObj.loadedEntityList) {
						if (entity instanceof EntityPlayer) {
							continue;
						}
						for (int i = 2; i < disease.getParameters("mob").length;i++) {
							if (EntityList.getEntityString(entity) != null && EntityList.getEntityString(entity).equalsIgnoreCase(((JsonPrimitive) disease.getParameters("mob")[i]).getAsString()) && entity.getDistanceToEntity(event.entityLiving) <= distance && GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive) disease.getParameters("mob")[1]).getAsInt()) {
								DiseaseHelper.addDisease(event.entityLiving, disease);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@SubscribeEvent
	public void biomeContraction(LivingEvent.LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			if (event.entityLiving.worldObj.getTotalWorldTime() % 20 == 0) {
				for (Disease disease : Diseases.diseases) {
					if (disease.getWaysToContract() != null && disease.getParameters("temp") != null && disease.getWaysToContract().contains("temp")) {
						float temperature = ((JsonPrimitive) disease.getParameters("temp")[0]).getAsFloat();
						int randomNumber = ((JsonPrimitive) disease.getParameters("temp")[1]).getAsInt();
						boolean warmDetection = ((JsonPrimitive) disease.getParameters("temp")[2]).getAsBoolean();
						float temp = event.entityLiving.worldObj.getBiomeGenForCoordsBody(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posZ)).getFloatTemperature(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posY), MathHelper.floor_double(event.entityLiving.posZ));

						if (ConfigRegistry.useTempCompat) {
							temp = event.entityLiving.getEntityData().getFloat(ConfigRegistry.tempTag);
							temperature *= ConfigRegistry.baseTemp;
						}

						if (((warmDetection && temp >= temperature) || (!warmDetection && temp <= temperature)) && GenericEffects.rand.nextInt(1000000) >= randomNumber) {
							DiseaseHelper.addDisease(event.entityLiving, disease);
						}
					}

					// Stat Contraction

					if (disease.getWaysToContract() != null && disease.getParameters("stat") != null && disease.getWaysToContract().contains("stat")) {
						String statName = ((JsonPrimitive) disease.getParameters("stat")[0]).getAsString();
						int statValue = ((JsonPrimitive) disease.getParameters("stat")[1]).getAsInt();
						boolean useBooleanFlag = ((JsonPrimitive) disease.getParameters("stat")[2]).getAsBoolean();
						int randomNumber = ((JsonPrimitive) disease.getParameters("stat")[3]).getAsInt();
						if (event.entityLiving instanceof EntityPlayerMP) {
							EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
							if (player.func_147099_x().writeStat(StatHelper.getStatBaseFromName(statName)) >= statValue) {
								if (GenericEffects.rand.nextInt(1000000) > randomNumber) {
									if (useBooleanFlag) {
										if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10) || !player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean("has" + disease.getId())) {
											if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10)) {
												player.getEntityData().setTag(player.PERSISTED_NBT_TAG, new NBTTagCompound());
											}
											player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setBoolean("has" + disease.getId(), true);
										}
									}

									DiseaseHelper.addDisease(event.entityLiving, disease);
								}
							}
						}
					}

				}

			}
		}
	}

	@SubscribeEvent
	public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		for (Disease disease : Diseases.diseases) {
			if (DiseaseHelper.isDiseaseActive(event.entityLiving,disease)) {
				if (event.entityLiving.getEntityData().hasKey(disease.getUnlocalizedName().replaceAll(".name","")) && event.entityLiving.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) == 1 && !event.entityLiving.isDead) {
					MinecraftForge.EVENT_BUS.post(new DiseaseEvent.DiseaseEndEvent(disease,event.entityLiving));
				}
				GenericEffects.applyEffects(event.entityLiving,disease);
				int newDiseaseTimer = event.entityLiving.getEntityData().getInteger(disease.getUnlocalizedName().replaceAll(".name","")) - 1;
				event.entityLiving.getEntityData().setInteger(disease.getUnlocalizedName().replaceAll(".name",""),newDiseaseTimer);
			}
		}
		if (event.entityLiving.worldObj.getTotalWorldTime() % 60 == 0) {
			if (!event.entityLiving.worldObj.isRemote) {
				if (event.entityLiving instanceof EntityPlayer) {
					EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
					PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG, player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)), player);
				}
			}
		}
		/*
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (DiseaseHelper.isDiseaseActive(event.entityLiving, DiseaseHelper.getDiseaseInstance("brainReanimation"))) {
				net.minecraft.client.Minecraft.getMinecraft().thePlayer.movementInput =
			}
		} */
	}

	@SubscribeEvent
	public void login(PlayerEvent.PlayerLoggedInEvent event) {
		/*if (VersionJSON.versions != null) {
			double biggestDouble = 2.0;
			for (DCVersion version : VersionJSON.versions) {
				if (version.mcVersion == Double.parseDouble(MinecraftForge.MC_VERSION.substring(MinecraftForge.MC_VERSION.indexOf(".") + 1))) {
					if (version.versionNumber > biggestDouble) {
						biggestDouble = version.versionNumber;
					}
				}
			}
			event.player.addChatComponentMessage(new ChatComponentTranslation("disease.update.new", biggestDouble));
			event.player.addChatComponentMessage(new ChatComponentText(VersionJSON.getVersion(biggestDouble) != null ? VersionJSON.getVersion(biggestDouble).updateString : "[ERROR: Changelog not available]"));
		} */
	}

	@SubscribeEvent
	public void onEaten(PlayerUseItemEvent.Finish event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			if (event.item != null) {
				for (Disease disease : Diseases.diseases) {
					if (disease.getWaysToContract() != null && disease.getWaysToContract().contains("eaten")) {
						if (disease.getParameters("eaten") != null) {
							//event.item.getItem() == Item.getItemById(itemID) &&
							int itemID = -1;
							if (isInteger(((JsonPrimitive)disease.getParameters("eaten")[0]).getAsString())) {
								itemID = ((JsonPrimitive) disease.getParameters("eaten")[0]).getAsInt();
							}
							int itemDamage = ((JsonPrimitive) disease.getParameters("eaten")[1]).getAsInt();
							int randomChance = ((JsonPrimitive) disease.getParameters("eaten")[2]).getAsInt();
							if ((itemID != -1 && event.item.getItem() == Item.getItemById(itemID)) || (GameData.getItemRegistry().getObject(((JsonPrimitive) disease.getParameters("eaten")[0]).getAsString().replaceAll("\"", "")) != null && event.item.getItem() == GameData.getItemRegistry().getObject(((String) disease.getParameters("eaten")[0]).replaceAll("\"", "")))) {
								if (event.item.getItemDamage() == itemDamage && GenericEffects.rand.nextInt(1000000) > randomChance) {
									DiseaseHelper.addDisease(event.entityLiving, disease);
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void endDiseases(DiseaseEvent.DiseaseEndEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if (!player.getEntityData().hasKey(player.PERSISTED_NBT_TAG, 10)) {
				player.getEntityData().setTag(player.PERSISTED_NBT_TAG, new NBTTagCompound());
			}
			player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).setBoolean(event.disease.getUnlocalizedName().replaceAll(".name", ".complete"), true);
			//System.out.println("Goodbye Caroline!");
			//System.out.println(event.entityLiving + " " + player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG).getBoolean(event.disease.getUnlocalizedName().replaceAll(".name", ".complete")));
			if (!player.worldObj.isRemote) {
				PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG,player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)),(EntityPlayerMP)player.worldObj.getClosestPlayer(player.posX,player.posY,player.posZ,10));
			}
		}
	}

	boolean isInteger(String intString) {
		try {
			Integer.parseInt(intString,10);
		} catch(Exception ex) {
			return false;
		}
		return true;
	}

	@SubscribeEvent
	public void zombieHurt(LivingHurtEvent event) {
		/*
		if (DiseaseHelper.isDiseaseActive(event.entityLiving,DiseaseHelper.getDiseaseInstance("brainReanimation"))) {
			if (event.source.getEntity() instanceof EntityZombie) {
				event.setCanceled(true);
			}
		} */
	}

	@SubscribeEvent
	public void inputEvent(net.minecraftforge.client.event.MouseEvent event) {
		/*
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getMinecraft();
			if (DiseaseHelper.isDiseaseActive(minecraft.thePlayer,DiseaseHelper.getDiseaseInstance("brainReanimation"))) {

				event.setCanceled(true);
			}
		} */
	}

	@SubscribeEvent
	public void startGame(WorldEvent.Load event) {
		if (!diseasedCrops.containsKey(event.world)) {
			File file = new File(event.world.getSaveHandler().getWorldDirectory(), "/DC/diseasedPlants.json");
			if (file.exists()) {
				try {
					GsonBuilder builder = new GsonBuilder();
					builder.registerTypeAdapter(BlockPos.class, new PositionJSON());
					Gson gson = builder.create();

					diseasedCrops.put(event.world, Arrays.asList(gson.fromJson(new FileReader(file), BlockPos[].class)));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void startGame(WorldEvent.Unload event) {
		if (!event.world.isRemote && diseasedCrops.containsKey(event.world)) {
			File file = new File(event.world.getSaveHandler().getWorldDirectory(),"/DC/diseasedPlants.json");

			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}

			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(BlockPos.class, new PositionJSON());
			Gson gson = gsonBuilder.create();


			try
			{
				file.createNewFile();
				BufferedWriter bReader = new BufferedWriter(new FileWriter(file));
				JsonWriter reader = new JsonWriter(bReader);
				//bReader.write("[ \n");
				Type type = new TypeToken<List<BlockPos>>(){}.getType();
				//for (BlockPos position : diseasedCrops.get(event.world)) {
					//if (event.world.getBlock(position.x,position.y,position.z) instanceof IPlantable) {
				gson.toJson(diseasedCrops.get(event.world), type, reader);
						//if (diseasedCrops.get(event.world).indexOf(position) != diseasedCrops.get(event.world).size() - 1) {
							//bReader.write(",\n");
						//}
					//}
				//}
				//bReader.write("\n]");
				reader.close();
				System.out.println("Finished writing stuff!");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.worldObj.isRemote) {
			if (!diseasedCrops.containsKey(event.player.worldObj)) {
				File file = new File(event.player.worldObj.getSaveHandler().getWorldDirectory(), "/DC/diseasedPlants.json");
				if (file.exists()) {
					try {
						GsonBuilder builder = new GsonBuilder();
						builder.registerTypeAdapter(BlockPos.class, new PositionJSON());
						Gson gson = builder.create();

						diseasedCrops.put(event.player.worldObj, Arrays.asList(gson.fromJson(new FileReader(file), BlockPos[].class)));

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (!event.player.worldObj.isRemote && event.player.worldObj.getTotalWorldTime() % 40 == 0) {
			//System.out.println("Fired a tick!");
			int xPos = MathHelper.floor_double(event.player.posX);
			int yPos = MathHelper.floor_double(event.player.posY);
			int zPos = MathHelper.floor_double(event.player.posZ);
			for (int x = xPos - 5; x < xPos + 6;x++) {
				for (int y = yPos - 5; y < yPos + 6;y++) {
					for (int z = zPos - 5; z < zPos + 6;z++) {
						World world = event.player.worldObj;
						if ((world.getBlock(x,y,z) instanceof IPlantable) && !alreadyDiseased(world,x,y,z)) {
							//System.out.println("Found suitable candidate");
							if (world.rand.nextInt(100) < ConfigRegistry.diseasedPlantsChance) {
								if (diseasedCrops.containsKey(world)) {
									List<BlockPos> blockPoses = new ArrayList<BlockPos>(diseasedCrops.get(world));
									blockPoses.add(new BlockPos(x,y,z));
									diseasedCrops.remove(world);
									diseasedCrops.put(world,blockPoses);
								} else {
									List<BlockPos> blockPoses = new ArrayList<BlockPos>();
									blockPoses.add(new BlockPos(x,y,z));
									diseasedCrops.put(world,blockPoses);
								}
								//System.out.println("Added " + x + ", " + y + ", " + z + " to the list!");
							}
						}
					}
				}
			}
			if (diseasedCrops.containsKey(event.player.worldObj)) {
				List<BlockPos> poses = new ArrayList<BlockPos>();
				for (BlockPos pos : diseasedCrops.get(event.player.worldObj)) {
					if (event.player.worldObj.getBlock(pos.x, pos.y, pos.z) instanceof IPlantable)
						poses.add(pos);
				}
				diseasedCrops.remove(event.player.worldObj);
				diseasedCrops.put(event.player.worldObj,poses);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent event) {
		if (event.world.getTotalWorldTime() % 30 == 0) {
			if (diseasedCrops.containsKey(event.world)) {
				for (BlockPos pos : diseasedCrops.get(event.world)) {
						AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(pos.x - 1.5, pos.y - 0.5, pos.z - 1.5, pos.x + 1.5, pos.y + 1.5, pos.z + 1.5);
						List<EntityLivingBase> entities = event.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

						if ((GameData.getBlockRegistry().getObject(event.world.getBlock(pos.x, pos.y, pos.z).getUnlocalizedName()) instanceof IPlantable)) {
							for (EntityLivingBase entity : entities) {
								for (Disease disease : Diseases.diseases) {
									if (disease.getWaysToContract().contains("crops")) {
										int chance = ((JsonPrimitive) disease.getParameters("crops")[0]).getAsInt();
										String[] array = StringUtils.getStringsFromPrimitives((JsonPrimitive[]) StringUtils.cutArray(disease.getParameters("crops"), 1));
										if (StringUtils.arrayContainsLoose(array, event.world.getBlock(pos.x, pos.y, pos.z).getUnlocalizedName())) {
											if (!event.world.isRemote && event.world.rand.nextInt(1000000) > chance) {
												DiseaseHelper.addDisease(entity, disease);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (event.world.getTotalWorldTime() % 1200 == 0) {
			if (diseasedCrops.containsKey(event.world)) {
				for (BlockPos pos : diseasedCrops.get(event.world)) {
					event.world.playAuxSFX(2005, pos.x, pos.y, pos.z, 0);
				}
			}
		}
	}

	public boolean alreadyDiseased(World world, int x, int y, int z) {
		if (!diseasedCrops.containsKey(world))
			return false;
		for (BlockPos blocks : diseasedCrops.get(world)) {
			if (blocks.x == x && y == blocks.y && z == blocks.z)
				return true;
			else {
				Vec3 oldPos = Vec3.createVectorHelper(blocks.x,blocks.y,blocks.z);
				Vec3 newPos = Vec3.createVectorHelper(x,y,z);
				if (newPos.distanceTo(oldPos) < 5) {
					return true;
				}
			}
		}
		return false;
	}

}
