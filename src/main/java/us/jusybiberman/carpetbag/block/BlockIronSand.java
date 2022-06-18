package us.jusybiberman.carpetbag.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import us.jusybiberman.carpetbag.item.CarpetbagTab;

import static us.jusybiberman.carpetbag.Carpetbag.MOD_ID;

public class BlockIronSand extends Block {
	public BlockIronSand() {
		super(Material.SAND, MapColor.SAND);
		setRegistryName(MOD_ID, "iron_sand");
		setTranslationKey("iron_sand");
		setCreativeTab(CarpetbagTab.tabCarpetbag);
		setSoundType(SoundType.SAND);
		setHardness(0.7F);
		setResistance(5.0F);
		setHarvestLevel("shovel", 0);
	}
}
