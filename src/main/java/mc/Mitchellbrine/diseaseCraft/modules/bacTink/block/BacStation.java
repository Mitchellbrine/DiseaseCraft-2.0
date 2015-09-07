package mc.Mitchellbrine.diseaseCraft.modules.bacTink.block;

import mc.Mitchellbrine.diseaseCraft.modules.BacterialTinkerer;
import mc.Mitchellbrine.diseaseCraft.utils.References;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Mitchellbrine on 2015.
 */
public class BacStation extends BlockContainer {

	public BacStation() {
		super(Material.iron);
		setBlockName("bacStation");
		setHardness(4.0F);
		setCreativeTab(BacterialTinkerer.tab);
		setBlockTextureName(References.MODID + ":bacStation");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return null;
	}
}
