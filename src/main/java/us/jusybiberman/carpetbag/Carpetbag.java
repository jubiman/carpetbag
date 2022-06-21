package us.jusybiberman.carpetbag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.jusybiberman.carpetbag.block.BlockIronSand;
import us.jusybiberman.carpetbag.block.BlockKera;
import us.jusybiberman.carpetbag.block.ModBlocks;
import us.jusybiberman.carpetbag.block.tatara.BlockTatara;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.client.GuiHandler;
import us.jusybiberman.carpetbag.commands.CommandSkillExp;
import us.jusybiberman.carpetbag.commands.debug.CommandCarpetbagData;
import us.jusybiberman.carpetbag.commands.debug.CommandCreateDungeon;
import us.jusybiberman.carpetbag.commands.debug.CommandDeleteDungeon;
import us.jusybiberman.carpetbag.crafting.manager.CraftingManagerTatara;
import us.jusybiberman.carpetbag.dungeons.DungeonManager;
import us.jusybiberman.carpetbag.dungeons.ModDimensions;
import us.jusybiberman.carpetbag.interaction.ModInteractions;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.item.ModItems;
import us.jusybiberman.carpetbag.network.NetworkHandler;
import us.jusybiberman.carpetbag.proxy.IProxy;
import us.jusybiberman.carpetbag.skills.SkillMining;
import us.jusybiberman.carpetbag.block.tatara.TileEntityTatara;
import us.jusybiberman.carpetbag.config.ModConfiguration;
import us.jusybiberman.carpetbag.util.Scheduler;

import static us.jusybiberman.carpetbag.material.Materials.*;

@Mod(
		modid = Carpetbag.MOD_ID,
		name = Carpetbag.MOD_NAME,
		version = Carpetbag.VERSION,
		guiFactory = "us.jusybiberman.carpetbag.client.GuiFactory"
)
public class Carpetbag {

	public static final String MOD_ID = "carpetbag";
	public static final String MOD_NAME = "Carpetbag";
	public static final String VERSION = "1.0-SNAPSHOT";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final DungeonManager DUNGEON_MANAGER = new DungeonManager();
	public static final Scheduler SCHEDULER = new Scheduler();
	/**
	 * This is the instance of your mod as created by Forge. It will never be null.
	 */
	@Mod.Instance(MOD_ID)
	public static Carpetbag INSTANCE;
	public static MinecraftServer SERVER_INSTANCE;

	public static Logger getLogger() {
		return LOGGER;
	}
	@SidedProxy(clientSide = "us.jusybiberman.carpetbag.proxy.ClientProxy", serverSide = "us.jusybiberman.carpetbag.proxy.ServerProxy")
	public static IProxy proxy;


	/**
	 * This is the first initialization event. Register tile entities here.
	 * The registry events below will have fired prior to entry to this method.
	 */
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		getLogger().debug("PRE INIT");

		ModInteractions.prePreInit(event);

		NetworkHandler.registerPackets();
		ModConfiguration.init(event);
		ModDimensions.init();
		CPBCapabilityManager.init();

		ModInteractions.preInit(event);
		ModInteractions.preInitEnd(event);
	}

	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		getLogger().debug("INIT");

		registerGuis();
		registerTileEntities();
		registerLootTables();
		registerEventListeners();
		registerOreDicts();
		registerRecipes();

		ModInteractions.init(event);
	}

	/**
	 * This is the final initialization event. Register actions from other mods here
	 */
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		ModInteractions.postInit(event);

		getLogger().debug("FINISHED LOADING THIS PIECE OF SHIT MOD CALLED CARPETBAG");
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		ModInteractions.loadComplete(event);
	}

	@Mod.EventHandler
	public void serverInit(FMLServerStartingEvent event) {
		SERVER_INSTANCE = event.getServer();
		event.registerServerCommand(new CommandSkillExp());
		event.registerServerCommand(new CommandCarpetbagData());
		event.registerServerCommand(new CommandCreateDungeon());
		event.registerServerCommand(new CommandDeleteDungeon());
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		ModDimensions.initDimensions();
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		DungeonManager.cleanupDimensionInformation();
		DungeonManager.clearInstance();
	}

	private static void registerGuis() {
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTatara.class, new ResourceLocation(MOD_ID, "tatara"));
	}

	private static void registerLootTables() {
		LootTableList.register(new ResourceLocation(MOD_ID, "kera"));
	}

	private static void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(SkillMining.class);
	}

	private static void registerOreDicts() {
		OreDictionary.registerOre("ingotTamahagane", ModItems.tamahagane_finished);
		OreDictionary.registerOre("ingotHochoTetsu", ModItems.hocho_tetsu_finished);
	}

	private static void registerRecipes() {
		// CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.IRON_SAND, 1)}, ironSandInput, SAND_PER_IRONSAND);
		// CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"tatara"),new ItemStack(ModBlocks.TATARA), "idi", "g g", "ini", 'i', "ingotIron", 'g', "ingotGold", 'd', "gemDiamond", 'n', new ItemStack(Blocks.NETHERRACK)),4);

		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.iron_sand), new ItemStack(ModBlocks.kera), 1);
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.tamahagane), new ItemStack(ModItems.tamahagane_heated), 15);
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.tamahagane_wrapped), new ItemStack(ModItems.tamahagane_reheated), 30);
		CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(ModItems.hocho_tetsu), new ItemStack(ModItems.hocho_tetsu_heated), 10);
		//CraftingManagerTatara.getInstance().addRecipe(Ingredient.fromItem(Items.CLAY_BALL), ModItems.TEA_CUP.getEmpty());

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

			// Block items
			event.getRegistry().register(ModItems.iron_sand.setRegistryName(MOD_ID, "iron_sand").setTranslationKey("iron_sand"));
			event.getRegistry().register(ModItems.kera.setRegistryName(MOD_ID, "kera").setCreativeTab(CarpetbagTab.tabCarpetbag).setTranslationKey("kera"));
			event.getRegistry().register(ModItems.tatara.setRegistryName(MOD_ID, "tatara").setTranslationKey("tatara"));

			// Materials
			event.getRegistry().register(ModItems.tamahagane.setRegistryName(MOD_ID, "tamahagane").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane"));
			event.getRegistry().register(ModItems.tamahagane_heated.setRegistryName(MOD_ID, "tamahagane_heated").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_heated"));
			event.getRegistry().register(ModItems.tamahagane_folded.setRegistryName(MOD_ID, "tamahagane_folded").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_folded"));
			event.getRegistry().register(ModItems.tamahagane_wrapped.setRegistryName(MOD_ID, "tamahagane_wrapped").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_wrapped"));
			event.getRegistry().register(ModItems.tamahagane_reheated.setRegistryName(MOD_ID, "tamahagane_reheated").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_reheated"));
			event.getRegistry().register(ModItems.tamahagane_finished.setRegistryName(MOD_ID, "tamahagane_finished").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_finished"));
			event.getRegistry().register(ModItems.hocho_tetsu.setRegistryName(MOD_ID, "hocho_tetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("hocho_tetsu"));
			event.getRegistry().register(ModItems.hocho_tetsu_heated.setRegistryName(MOD_ID, "hocho_tetsu_heated").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("hocho_tetsu_heated"));
			event.getRegistry().register(ModItems.hocho_tetsu_finished.setRegistryName(MOD_ID, "hocho_tetsu_finished").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("hocho_tetsu_finished"));

			event.getRegistry().register(ModItems.rice_ash.setRegistryName(MOD_ID, "rice_ash").setCreativeTab(CarpetbagTab.tabCarpetbag).setTranslationKey("rice_ash"));
			event.getRegistry().register(ModItems.washi.setRegistryName(MOD_ID, "washi").setCreativeTab(CarpetbagTab.tabCarpetbag).setTranslationKey("washi"));

			event.getRegistry().register(ModItems.akame_satetsu.setRegistryName(MOD_ID, "akame_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("akame_satetsu"));
			event.getRegistry().register(ModItems.masa_satetsu.setRegistryName(MOD_ID, "masa_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("masa_satetsu"));

			// Tools
			event.getRegistry().register(ModItems.onimaru_kunitsuna);

			// TODO: Temporary
			event.getRegistry().register(ModItems.material_japan.setRegistryName(MOD_ID, "material_japan"));
			tamahaganeToolMaterial.setRepairItem(ModItems.material_japan.getMaterial("tamahagane_finished"));
			bambooToolMaterial.setRepairItem(ModItems.material_japan.getMaterial("bamboo_slats"));
			japansteelToolMaterial.setRepairItem(ModItems.material_japan.getMaterial("tamahagane_finished"));
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

			// Blocks
			event.getRegistry().register(new BlockIronSand());
			event.getRegistry().register(new BlockKera());

			// Machines
			event.getRegistry().register(new BlockTatara());
		}

		@SubscribeEvent
		public static void addModels(ModelRegistryEvent event) {
			// Tools
			proxy.registerModel(ModItems.onimaru_kunitsuna, 0);

			// Block items
			proxy.registerModel(ModItems.iron_sand, 0);
			proxy.registerModel(ModItems.kera, 0);

			// Materials
			proxy.registerModel(ModItems.tamahagane, 0);
			proxy.registerModel(ModItems.tamahagane_heated, 0);
			proxy.registerModel(ModItems.tamahagane_folded, 0);
			proxy.registerModel(ModItems.tamahagane_wrapped, 0);
			proxy.registerModel(ModItems.tamahagane_reheated, 0);
			proxy.registerModel(ModItems.tamahagane_finished, 0);

			proxy.registerModel(ModItems.hocho_tetsu, 0);
			proxy.registerModel(ModItems.hocho_tetsu_heated, 0);
			proxy.registerModel(ModItems.hocho_tetsu_finished, 0);

			proxy.registerModel(ModItems.rice_ash, 0);
			proxy.registerModel(ModItems.washi, 0);

			proxy.registerModel(ModItems.akame_satetsu, 0);
			proxy.registerModel(ModItems.masa_satetsu, 0);

			// Machines
			proxy.registerModel(ModItems.tatara, 0);
		}
	}
}
