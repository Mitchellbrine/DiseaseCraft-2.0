package mc.Mitchellbrine.diseaseCraft.utils;

import mc.Mitchellbrine.diseaseCraft.event.ContractingEvents;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchellbrine on 2015.
 */
public class PosHelper {

	public static void addBlockPos(World world, int x, int y, int z) {
		List<BlockPos> blockPosList = ContractingEvents.diseasedCrops.get(world);
		if (blockPosList == null) {
			blockPosList = new ArrayList<BlockPos>();
		}
		blockPosList.add(new BlockPos(x,y,z));
		if (!ContractingEvents.diseasedCrops.containsKey(world)) {
			ContractingEvents.diseasedCrops.put(world,blockPosList);
		}
	}

	public static void addBlockPos(World world, int x, int y, int z,boolean isForced) {
		List<BlockPos> blockPosList = ContractingEvents.diseasedCrops.get(world);
		if (blockPosList == null) {
			blockPosList = new ArrayList<BlockPos>();
		}
		BlockPos pos = new BlockPos(x,y,z);
		pos.isForced = isForced;
		blockPosList.add(pos);
		if (!ContractingEvents.diseasedCrops.containsKey(world)) {
			ContractingEvents.diseasedCrops.put(world,blockPosList);
		}
	}

	public static void addForcedBlockPos(World world, int x, int y, int z) {
		List<BlockPos> blockPosList = ContractingEvents.diseasedCrops.get(world);
		if (blockPosList == null) {
			blockPosList = new ArrayList<BlockPos>();
		}
		BlockPos pos = new BlockPos(x,y,z);
		pos.isForced = true;
		blockPosList.add(pos);
		if (!ContractingEvents.diseasedCrops.containsKey(world)) {
			ContractingEvents.diseasedCrops.put(world,blockPosList);
		}
	}

}
