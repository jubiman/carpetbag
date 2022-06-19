package us.jusybiberman.carpetbag.block;

import net.minecraftforge.fml.common.registry.GameRegistry;
import us.jusybiberman.carpetbag.block.tatara.BlockTatara;

import static us.jusybiberman.carpetbag.Carpetbag.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public class ModBlocks {
	@GameRegistry.ObjectHolder("iron_sand")
	public static final BlockIronSand iron_sand = null;
	@GameRegistry.ObjectHolder("tatara")
	public static final BlockTatara tatara = null;
	@GameRegistry.ObjectHolder("kera")
	public static final BlockKera kera = null;
}