package mc.Mitchellbrine.diseaseCraft.event;

import com.google.gson.JsonPrimitive;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import mc.Mitchellbrine.diseaseCraft.api.Disease;
import mc.Mitchellbrine.diseaseCraft.api.DiseaseEvent;
import mc.Mitchellbrine.diseaseCraft.disease.DiseaseHelper;
import mc.Mitchellbrine.diseaseCraft.disease.Diseases;
import mc.Mitchellbrine.diseaseCraft.disease.effects.GenericEffects;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitchellbrine on 2015.
 */
public class ContractingEvents {

	private int second = 0;

	public static Map<World,List<BlockPos>> diseasedCrops;

	static {
		diseasedCrops = new HashMap<World, List<BlockPos>>();
	}


	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			if (Diseases.diseaseTypes.containsKey("mobAttack")) {
				for (Disease disease : Diseases.diseaseTypes.get("mobAttack")) {
					if (disease.getParameters("mobAttack") == null || !disease.getWaysToContract().contains("mobAttack"))
						continue;


					if (event.source != null && event.source.getEntity() != null && Diseases.entityClasses.contains(event.source.getEntity().getClass()) && Diseases.mobAttackClasses.contains(event.source.getEntity().getClass())) {

						int chance;

						if (((JsonPrimitive) disease.getParameters("mobAttack")[0]).isNumber()) {
							chance = ((JsonPrimitive)disease.getParameters("mobAttack")[0]).getAsInt();
						} else {
							chance = ((JsonPrimitive)disease.getParameters("mobAttack")[1]).getAsInt();

						}

						GenericEffects.rand.setSeed(event.entityLiving.worldObj.getTotalWorldTime());
						if (GenericEffects.rand.nextInt(1000000) >= chance) {
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
		if (!event.entityLiving.worldObj.isRemote && Diseases.entityClasses.contains(event.entityLiving.getClass()) && Diseases.mobClasses.contains(event.entityLiving.getClass())) {
			if (event.entityLiving.worldObj.getTotalWorldTime() % 22 == 0) {
				if (Diseases.diseaseTypes.containsKey("mob")) {
					for (Disease disease : Diseases.diseaseTypes.get("mob")) {
							int distance;
							AxisAlignedBB aabb;
							int chance;


							if (((JsonPrimitive) disease.getParameters("mob")[0]).isString()) {
								distance = ((JsonPrimitive) disease.getParameters("mob")[1]).getAsInt();

								aabb = AxisAlignedBB.getBoundingBox(event.entityLiving.posX - distance, event.entityLiving.posY - distance, event.entityLiving.posZ - distance, event.entityLiving.posX + distance, event.entityLiving.posY + distance, event.entityLiving.posZ + distance);
								chance = ((JsonPrimitive)disease.getParameters("mob")[2]).getAsInt();

									/**if (entity instanceof EntityPlayer) {
										continue;
									}
									if (((JsonPrimitive) disease.getParameters("mob")[0]).getAsString() == null || !EntityList.classToStringMapping.entrySet().contains(((JsonPrimitive) disease.getParameters("mob")[0]).getAsString()))
										continue;
									//System.out.println(disease.getUnlocalizedName());
									//System.out.println(i);
									if (EntityList.getEntityString(event.entityLiving) != null && EntityList.getEntityString(entity).equalsIgnoreCase(((JsonPrimitive) disease.getParameters("mob")[0]).getAsString()) && /**entity.getDistanceToEntity(event.entityLiving) <= distance && GenericEffects.rand.nextInt(1000000) >= ((JsonPrimitive) disease.getParameters("mob")[2]).getAsInt()) {
										DiseaseHelper.addDisease(event.entityLiving, disease);
									}*/

							} else {
								distance = ((JsonPrimitive) disease.getParameters("mob")[0]).getAsInt();
								aabb = AxisAlignedBB.getBoundingBox(event.entityLiving.posX - distance, event.entityLiving.posY - distance, event.entityLiving.posZ - distance, event.entityLiving.posX + distance, event.entityLiving.posY + distance, event.entityLiving.posZ + distance);
								chance = ((JsonPrimitive)disease.getParameters("mob")[1]).getAsInt();
							}


						for (Entity entity : (List<Entity>)event.entityLiving.worldObj.getEntitiesWithinAABBExcludingEntity(event.entityLiving,aabb)) {
							if (entity instanceof EntityLivingBase) {
								if (GenericEffects.rand.nextInt(1000000) >= chance) {
									DiseaseHelper.addDisease((EntityLivingBase)entity,disease);
								}
							}
						}
							//}
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
				switch (second) {
					case 1:
						if (Diseases.diseaseTypes.containsKey("stat")) {
							for (Disease disease : Diseases.diseaseTypes.get("stat")) {
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
						break;
					case 2:
						if (Diseases.diseaseTypes.containsKey("block-contact")) {
							for (Disease disease : Diseases.diseaseTypes.get("block-contact")) {
								int radius = ((JsonPrimitive) disease.getParameters("block-contact")[0]).getAsInt();
								int randomNumber = ((JsonPrimitive) disease.getParameters("block-contact")[1]).getAsInt();
								String[] blocks = StringUtils.getStringsFromPrimitives((JsonPrimitive[]) StringUtils.cutArray(disease.getParameters("block-contact"), 2));
								for (int xx = event.entityLiving.serverPosX - radius; xx <= event.entityLiving.serverPosX + radius; xx++) {
									for (int yy = event.entityLiving.serverPosY - radius; yy <= event.entityLiving.serverPosY + radius; yy++) {
										for (int zz = event.entityLiving.serverPosZ - radius; zz <= event.entityLiving.serverPosZ + radius; zz++) {
											if (StringUtils.arrayContainsLoose(blocks, event.entityLiving.worldObj.getBlock(xx, yy, zz).getUnlocalizedName()) && event.entityLiving.worldObj.rand.nextInt(1000000) > randomNumber) {
												DiseaseHelper.addDisease(event.entityLiving, disease);
											}
										}
									}
								}
							}
						}
						break;
					default:
						/*if (Diseases.diseaseTypes.containsKey("temp")) {
							for (Disease disease : Diseases.diseaseTypes.get("temp")) {
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
						}*/
						break;
				}
				if (second < 2) {
					second++;
				} else {
					second = 0;
				}
			}

			if (event.entityLiving.worldObj.getTotalWorldTime() % 60 == 0) {
				if (event.entityLiving instanceof EntityPlayer) {
					EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
					PacketHandler.INSTANCE.sendTo(new NBTPacket(player.PERSISTED_NBT_TAG, player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG)), player);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingUpdate(TickEvent.WorldTickEvent event) {
		if (!event.world.isRemote) {
			if (Diseases.diseasedEntities.containsKey(event.world)) {
				for (EntityLivingBase entity : Diseases.diseasedEntities.get(event.world)) {
					for (Disease disease : Diseases.diseases) {
						if (DiseaseHelper.isDiseaseActive(entity, disease)) {
							MinecraftForge.EVENT_BUS.register(new DiseaseEvent.DiseaseTickEvent(disease, entity));
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void diseaseTick(DiseaseEvent.DiseaseTickEvent event) {
		GenericEffects.applyEffects(event.entityLiving,event.disease);
		int newDiseaseTimer = event.entityLiving.getEntityData().getInteger(event.disease.getUnlocalizedName().replaceAll(".name","")) - 1;
		event.entityLiving.getEntityData().setInteger(event.disease.getUnlocalizedName().replaceAll(".name",""),newDiseaseTimer);
		if (event.entityLiving.getEntityData().hasKey(event.disease.getUnlocalizedName().replaceAll(".name","")) && event.entityLiving.getEntityData().getInteger(event.disease.getUnlocalizedName().replaceAll(".name","")) == 1 && !event.entityLiving.isDead) {
			MinecraftForge.EVENT_BUS.post(new DiseaseEvent.DiseaseEndEvent(event.disease,event.entityLiving));
		}
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
				if (Diseases.diseaseTypes.containsKey("eaten")) {
					for (Disease disease : Diseases.diseaseTypes.get("eaten")) {
						//event.item.getItem() == Item.getItemById(itemID) &&
						int itemID = -1;
						if (((JsonPrimitive) disease.getParameters("eaten")[0]).isNumber()) {
							itemID = ((JsonPrimitive) disease.getParameters("eaten")[0]).getAsInt();
						}
						int itemDamage = ((JsonPrimitive) disease.getParameters("eaten")[1]).getAsInt();
						int randomChance = ((JsonPrimitive) disease.getParameters("eaten")[2]).getAsInt();
						if ((itemID != -1 && event.item.getItem() == Item.getItemById(itemID)) || (GameData.getItemRegistry().getObject(((JsonPrimitive) disease.getParameters("eaten")[0]).getAsString().replaceAll("\"", "")) != null && event.item.getItem() == GameData.getItemRegistry().getObject(((JsonPrimitive) disease.getParameters("eaten")[0]).getAsString().replaceAll("\"", "")))) {
							if (event.item.getItemDamage() == itemDamage && GenericEffects.rand.nextInt(1000000) > randomChance) {
								DiseaseHelper.addDisease(event.entityLiving, disease);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void endDiseases(DiseaseEvent.DiseaseEndEvent event) {
		if (!event.entityLiving.worldObj.isRemote) {
			DiseaseHelper.removeDisease(event.entityLiving,event.disease);
		}
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
