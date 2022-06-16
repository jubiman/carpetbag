package us.jusybiberman.carpetbag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.jusybiberman.carpetbag.block.BlockIronSand;
import us.jusybiberman.carpetbag.commands.CommandSkillExp;
import us.jusybiberman.carpetbag.commands.debug.CommandCarpetbagData;
import us.jusybiberman.carpetbag.events.PlayerJoinEvent;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.item.swords.OnimaruKunitsuna;
import us.jusybiberman.carpetbag.material.Materials;
import us.jusybiberman.carpetbag.proxy.ClientProxy;
import us.jusybiberman.carpetbag.proxy.CommonProxy;
import us.jusybiberman.carpetbag.skills.SkillMining;

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
	/**
	 * This is the instance of your mod as created by Forge. It will never be null.
	 */
	@Mod.Instance(MOD_ID)
	public static Carpetbag INSTANCE;
	//@SideOnly(Side.SERVER)
	public static MinecraftServer SERVER_INSTANCE;

	public static Logger getLogger() {
		return LOGGER;
	}
	@SidedProxy(clientSide = "us.jusybiberman.carpetbag.proxy.ClientProxy", serverSide = "us.jusybiberman.carpetbag.proxy.CommonProxy")
	public static CommonProxy proxy;


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
		Materials.TAMAHAGANE.setRepairItem(new ItemStack(Carpetbag.Items.tamahagane_ingot));
		getLogger().debug("FINISHED LOADING THIS PIECE OF SHIT MOD CALLED CARPETBAG");
		registerEventListeners();
	}

	@Mod.EventHandler
	public void serverInit(FMLServerStartingEvent event) {
		SERVER_INSTANCE = event.getServer();
		event.registerServerCommand(new CommandSkillExp());
		event.registerServerCommand(new CommandCarpetbagData());
	}

	private static void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(SkillMining.class);
		MinecraftForge.EVENT_BUS.register(PlayerJoinEvent.class);
	}

	/**
	 * Forge will automatically look up and bind blocks to the fields in this class
	 * based on their registry name.
	 */
	@GameRegistry.ObjectHolder(MOD_ID)
	public static class Blocks {
		@GameRegistry.ObjectHolder("iron_sand")
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
		@GameRegistry.ObjectHolder("onimaru_kunitsuna")
		public static final OnimaruKunitsuna onimaru_kunitsuna = new OnimaruKunitsuna();
		@GameRegistry.ObjectHolder("tamahagane_ingot")
		public static final Item tamahagane_ingot = new Item();
		@GameRegistry.ObjectHolder("akame_satetsu")
		public static final Item akame_satetsu = new Item();
		@GameRegistry.ObjectHolder("masa_satetsu")
		public static final Item masa_satetsu = new Item();
		@GameRegistry.ObjectHolder("iron_sand")
		public static final ItemBlock iron_sand = new ItemBlock(Blocks.iron_sand);
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
			getLogger().debug("ADDING ITEMS");
			event.getRegistry().register(Items.onimaru_kunitsuna);

			event.getRegistry().register(Items.iron_sand.setRegistryName(MOD_ID, "iron_sand").setTranslationKey("iron_sand"));

			event.getRegistry().register(Items.tamahagane_ingot.setRegistryName(MOD_ID, "tamahagane_ingot").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_ingot"));
			event.getRegistry().register(Items.akame_satetsu.setRegistryName(MOD_ID, "akame_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("akame_satetsu"));
			event.getRegistry().register(Items.masa_satetsu.setRegistryName(MOD_ID, "masa_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("masa_satetsu"));

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
			getLogger().debug("ADDING BLOCKS");
			//event.getRegistry().register(new Block(Material.SAND, MapColor.SAND).setRegistryName(MOD_ID, "iron_sand").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));
			event.getRegistry().register(new BlockIronSand());
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
		}

		@SubscribeEvent
		public static void addModels(ModelRegistryEvent event) {
			proxy.registerModel(Items.onimaru_kunitsuna, 0);
			proxy.registerModel(Items.iron_sand, 0);
			proxy.registerModel(Items.akame_satetsu, 0);
			proxy.registerModel(Items.masa_satetsu, 0);
			proxy.registerModel(Items.tamahagane_ingot, 0);
		}
	}
}
