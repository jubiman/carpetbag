package us.jusybiberman.carpetbag.block;

import net.minecraft.block.Block;
<<<<<<< HEAD
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockIronSand extends Block {

	public BlockIronSand(Material p_i46399_1_, MapColor p_i46399_2_) {
		super(p_i46399_1_, p_i46399_2_);
=======
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import us.jusybiberman.carpetbag.item.CarpetbagTab;

import static us.jusybiberman.carpetbag.Carpetbag.MOD_ID;

public class BlockIronSand extends Block {
	public BlockIronSand() {
		super(Material.SAND, MapColor.SAND);
		setRegistryName(MOD_ID, "iron_sand");
		setCreativeTab(CarpetbagTab.tabCarpetbagMaterials);
		setSoundType(SoundType.SAND);
>>>>>>> 5acb3854655ab11c6d9c2c64bfe9be02b965aa98
	}
}
