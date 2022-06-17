package us.jusybiberman.carpetbag.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.block.ModBlocks;
import us.jusybiberman.carpetbag.item.swords.OnimaruKunitsuna;

import static us.jusybiberman.carpetbag.Carpetbag.MOD_ID;
import static us.jusybiberman.carpetbag.material.Materials.*;

@GameRegistry.ObjectHolder(MOD_ID)
public class ModItems {
	@GameRegistry.ObjectHolder("onimaru_kunitsuna")
	public static final OnimaruKunitsuna onimaru_kunitsuna = new OnimaruKunitsuna();
	@GameRegistry.ObjectHolder("tamahagane_ingot")
	public static final Item tamahagane_ingot = new Item();
	@GameRegistry.ObjectHolder("akame_satetsu")
	public static final Item akame_satetsu = new Item();
	@GameRegistry.ObjectHolder("masa_satetsu")
	public static final Item masa_satetsu = new Item();
	@GameRegistry.ObjectHolder("iron_sand")
	public static final ItemBlock iron_sand = new ItemBlock(ModBlocks.iron_sand);
	@GameRegistry.ObjectHolder("tatara")
	public static final ItemBlock tatara = new ItemBlock(ModBlocks.tatara);
	@GameRegistry.ObjectHolder("material_japan")
	public static ItemMaterial material_japan = new ItemMaterial(
			new String[]{"rice", "soaked_rice", "rice_stalk", "rice_hay", "rice_ash", "rush",
					"soaked_bamboo", "bamboo_slats", "soaked_mulberry", "mulberry_paste", "mulberry_sheet", "washi",
					"iron_scales", "lamellar", "paper_lamellar", "tsuka", "half_katana_blade", "ya_head", "yumi_top", "yumi_bottom",
					"tamahagane", "tamahagane_heated", "tamahagane_folded", "tamahagane_reheated", "tamahagane_finished", "tamahagane_wrapped",
					"hocho_tetsu", "hocho_tetsu_heated", "hocho_tetsu_fold_1", "hocho_tetsu_fold_2", "hocho_tetsu_finished",
					"helmet_undecorated", "chest_undecorated", "legs_undecorated", "boots_undecorated", "bark_sakura", "bark_mulberry"
			}
	);
}
