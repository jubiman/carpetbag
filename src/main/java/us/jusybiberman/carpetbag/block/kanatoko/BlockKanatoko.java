package us.jusybiberman.carpetbag.block.kanatoko;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import us.jusybiberman.carpetbag.block.BlockContainerBase;
import us.jusybiberman.carpetbag.item.CarpetbagTab;

public class BlockKanatoko extends BlockContainerBase {
	protected BlockKanatoko() {
		super("special_anvil", Material.ANVIL);
		setHardness(3.0F);
		setResistance(6.0F);
		setHarvestLevel("pickaxe", 1);
		setTranslationKey("special_anvil");
		setCreativeTab(CarpetbagTab.tabCarpetbagMachines);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityKanatoko();
	}
}
