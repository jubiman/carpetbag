package us.jusybiberman.carpetbag.block.tatara;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.block.BlockContainerBase;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.lib.GuiIds;
import us.jusybiberman.carpetbag.util.IHasVariants;
import us.jusybiberman.carpetbag.util.InvUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockTatara extends BlockContainerBase implements IHasVariants {
	public BlockTatara() {
		super("tatara", Material.IRON);
		setHardness(3.0F);
		setResistance(6.0F);
		setHarvestLevel("pickaxe", 1);
		setTranslationKey("tatara");
		setCreativeTab(CarpetbagTab.tabCarpetbagMachines);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityTatara();
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if(((TileEntityTatara) Objects.requireNonNull(worldIn.getTileEntity(pos))).isValidStructure())
				playerIn.openGui(Carpetbag.INSTANCE, GuiIds.TATARA, worldIn, pos.getX(), pos.getY(), pos.getZ());
			else
				playerIn.sendMessage(new TextComponentString("Structure is invalid."));
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null) {
			if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
				// TODO: fix this to where it gets sent to a person's lost and found or smth
				InvUtils.ejectInventoryContents(world, pos, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public List<ModelResourceLocation> getVariantModels() {
		ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

		rlist.add(new ModelResourceLocation(getRegistryName(), "normal"));

		return rlist;
	}

	@Override
	public String getVariantName(int meta) {
		return null;
	}
}
