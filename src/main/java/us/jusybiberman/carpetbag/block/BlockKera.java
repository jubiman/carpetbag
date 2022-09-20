package us.jusybiberman.carpetbag.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.init.ModItems;

import java.util.Random;

public class BlockKera extends Block {
	public BlockKera() {
		super(Material.ROCK, MapColor.STONE);

		setTranslationKey("kera");
		setRegistryName(Carpetbag.MOD_ID, "kera");
		setCreativeTab(CarpetbagTab.tabCarpetbag);
		setHardness(2.1F).setResistance(8.0F);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);
	}

	// TODO: Temporary?? cant seem to get loot tables working :(
	private static ItemStack getRandomMetal(Random random, boolean canEmpty) {
		double tamaThreshold = 0.3;
		double hochoThreshold = 0.5;
		double ironThreshold = 0.7;
		double totalWeight = 0.7;
		if (canEmpty)
			totalWeight = 1.0;
		double pick = random.nextDouble() * totalWeight;
		if (pick < tamaThreshold)
			return new ItemStack(ModItems.tamahagane);
		else if (pick < hochoThreshold)
			return new ItemStack(ModItems.hocho_tetsu);
		else if (pick < ironThreshold)
			return new ItemStack(Items.IRON_INGOT);
		else
			return ItemStack.EMPTY;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random rand = world instanceof World ? ((World)world).rand : RANDOM;

		drops.add(getRandomMetal(rand,false));
		for(int i = 1; i < 3; i++)
			drops.add(getRandomMetal(rand,true));
	}
}
