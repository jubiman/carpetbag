package us.jusybiberman.carpetbag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.jusybiberman.carpetbag.block.BlockIronSand;
import us.jusybiberman.carpetbag.block.tatara.BlockTatara;
import us.jusybiberman.carpetbag.client.GuiHandler;
import us.jusybiberman.carpetbag.commands.CommandSkillExp;
import us.jusybiberman.carpetbag.commands.debug.CommandCarpetbagData;
import us.jusybiberman.carpetbag.commands.debug.CommandCreateDungeon;
import us.jusybiberman.carpetbag.commands.debug.CommandDeleteDungeon;
import us.jusybiberman.carpetbag.dungeons.DungeonManager;
import us.jusybiberman.carpetbag.dungeons.ModDimensions;
import us.jusybiberman.carpetbag.events.PlayerJoinEvent;
import us.jusybiberman.carpetbag.item.CarpetbagTab;
import us.jusybiberman.carpetbag.item.ModItems;
import us.jusybiberman.carpetbag.proxy.IProxy;
import us.jusybiberman.carpetbag.skills.SkillMining;
import us.jusybiberman.carpetbag.block.tatara.TileEntityTatara;
import us.jusybiberman.carpetbag.util.Scheduler;

import java.io.File;

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
	public static File config; // TODO: might want to change this to a ConfigManager class
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
	@SidedProxy(clientSide = "us.jusybiberman.carpetbag.proxy.ClientProxy", serverSide = "us.jusybiberman.carpetbag.proxy.ServerProxy")
	public static IProxy proxy;


	/**
	 * This is the first initialization event. Register tile entities here.
	 * The registry events below will have fired prior to entry to this method.
	 */
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		getLogger().debug("PRE INIT");
		ModDimensions.init();
	}

	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		getLogger().debug("INIT");
		registerGuis();
		registerEventListeners();
		registerTileEntities();
	}

	/**
	 * This is the final initialization event. Register actions from other mods here
	 */
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		getLogger().debug("FINISHED LOADING THIS PIECE OF SHIT MOD CALLED CARPETBAG");
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

	private static void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(SkillMining.class);
		MinecraftForge.EVENT_BUS.register(PlayerJoinEvent.class);
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
			event.getRegistry().register(ModItems.onimaru_kunitsuna);

			event.getRegistry().register(ModItems.iron_sand.setRegistryName(MOD_ID, "iron_sand").setTranslationKey("iron_sand"));
			event.getRegistry().register(ModItems.tatara.setRegistryName(MOD_ID, "tatara").setTranslationKey("tatara"));

			event.getRegistry().register(ModItems.tamahagane_ingot.setRegistryName(MOD_ID, "tamahagane_ingot").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("tamahagane_ingot"));
			event.getRegistry().register(ModItems.akame_satetsu.setRegistryName(MOD_ID, "akame_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("akame_satetsu"));
			event.getRegistry().register(ModItems.masa_satetsu.setRegistryName(MOD_ID, "masa_satetsu").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials).setTranslationKey("masa_satetsu"));

			// TODO: Temporary
			event.getRegistry().register(ModItems.material_japan.setRegistryName(MOD_ID, "material_japan").setTranslationKey("material_japan"));
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
			//event.getRegistry().register(new Block(Material.SAND, MapColor.SAND).setRegistryName(MOD_ID, "iron_sand").setCreativeTab(CarpetbagTab.tabCarpetbagMaterials));
			event.getRegistry().register(new BlockIronSand());
			event.getRegistry().register(new BlockTatara());
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
		}

		@SubscribeEvent
		public static void addModels(ModelRegistryEvent event) {
			proxy.registerModel(ModItems.onimaru_kunitsuna, 0);
			proxy.registerModel(ModItems.iron_sand, 0);
			proxy.registerModel(ModItems.akame_satetsu, 0);
			proxy.registerModel(ModItems.masa_satetsu, 0);
			proxy.registerModel(ModItems.tamahagane_ingot, 0);
			proxy.registerModel(ModItems.tatara, 0);
		}
	}
}
