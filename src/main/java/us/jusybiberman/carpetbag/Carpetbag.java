package us.jusybiberman.carpetbag;

import net.minecraft.item.ItemBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.jusybiberman.carpetbag.block.BlockIronSand;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.item.swords.OnimaruKunitsuna;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(
		modid = Carpetbag.MOD_ID,
		name = Carpetbag.MOD_NAME,
		version = Carpetbag.VERSION
)
public class Carpetbag {

	public static final String MOD_ID = "carpetbag";
	public static final String MOD_NAME = "Carpetbag";
	public static final String VERSION = "1.0-SNAPSHOT";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * This is the instance of your mod as created by Forge. It will never be null.
	 */
	@Mod.Instance(MOD_ID)
	public static Carpetbag INSTANCE;

	/**
	 * This is the first initialization event. Register tile entities here.
	 * The registry events below will have fired prior to entry to this method.
	 */
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		getLogger().debug("PRE INIT");
	}

	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		getLogger().debug("INIT");
	}

	/**
	 * This is the final initialization event. Register actions from other mods here
	 */
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		//getLogger().debug("FINISHED LOADING THIS PIECE OF SHIT MOD CALLED CARPETBAG");
		//Materials.TAMAHAGANE.setRepairItem(new ItemStack(Carpetbag.Items.tamahagane_ingot));
		getLogger().debug("FINISHED LOADING THIS PIECE OF SHIT MOD CALLED CARPETBAG");
	}

	/**
	 * Forge will automatically look up and bind blocks to the fields in this class
	 * based on their registry name.
	 */
	@GameRegistry.ObjectHolder(MOD_ID)
	public static class Blocks {
		public static final BlockIronSand iron_sand = null;
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
	}

	/**
	 * Forge will automatically look up and bind items to the fields in this class
	 * based on their registry name.
	 */
	@GameRegistry.ObjectHolder(MOD_ID)
	public static class Items {
		public static final OnimaruKunitsuna onimaru_kunitsuna = new OnimaruKunitsuna();
		public static final Item tamahagane_ingot = null;
		public static final Item akame_satetsu = null;
		public static final Item masa_satetsu = null;
		public static final ItemBlock iron_sand = null;
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
	}

	/**
	 * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
	 */
	@Mod.EventBusSubscriber
	public static class ObjectRegistryHandler {
		/**
		 * Listen for the register event for creating custom items
		 */
		@SubscribeEvent
		public static void addItems(RegistryEvent.Register<Item> event) {
			event.getRegistry().register(Items.onimaru_kunitsuna);

			event.getRegistry().register(new ItemBlock(Blocks.iron_sand).setRegistryName(MOD_ID, "iron_sand"));

			event.getRegistry().register(new Item().setRegistryName(MOD_ID, "tamahagane_ingot").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));
			event.getRegistry().register(new Item().setRegistryName(MOD_ID, "akame_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));
			event.getRegistry().register(new Item().setRegistryName(MOD_ID, "masa_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));

			/*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
		}

		/**
		 * Listen for the register event for creating custom blocks
		 */
		@SubscribeEvent
		public static void addBlocks(RegistryEvent.Register<Block> event) {
			//event.getRegistry().register(new Block(Material.SAND, MapColor.SAND).setRegistryName(MOD_ID, "iron_sand").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));
			event.getRegistry().register(new BlockIronSand());
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
		}
	}
}
