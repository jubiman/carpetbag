package us.jusybiberman.carpetbag.dungeons;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.util.ModConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public class Dungeon extends WorldProvider {
	private long seed;
	public final DungeonWorldSavedData savedData = null;
	private Set<String> dimensionTypes = null;

	private long calculateSeed(long seed, int dim) {
		if (dimensionInformation == null || dimensionInformation.getWorldVersion() < DimensionInformation.VERSION_DIMLETSSEED) {
			return dim * 13L + seed;
		} else {
			return dimensionInformation.getDescriptor().calculateSeed(seed);
		}
	}

	public World getWorld() {
		return world;
	}

	private DimensionInformation dimensionInformation;
	private DimensionStorage storage;
	/*
	public Dungeon(int id, int dimid) {
		//new File(Objects.requireNonNull(getSaveFolder())).mkdirs();
		//savedData = new DungeonWorldSavedData("carpetbag_dungeon_" + id);
		active = true;
		ID = id;
		DIMENSION_ID = dimid;
		//this.biomeProvider = new BiomeProviderSingle(BiomeInit.COPPER);
	}*/

	private void completeDungeon() {
		dimensionInformation.setActive(false);
		Carpetbag.DUNGEON_MANAGER.scheduleDestroyDungeon(getDimension());
	}



	@Override
	public long getSeed() {
		if (dimensionInformation == null || dimensionInformation.getWorldVersion() < DimensionInformation.VERSION_CORRECTSEED) {
			return super.getSeed();
		} else {
			return seed;
		}
	}

	public void setDimensionInformation(DimensionInformation info) {
		dimensionInformation = info;
		//setupSkyRenderers();
	}

	public DimensionInformation getDimensionInformation() {
		if (dimensionInformation == null) {
			int dim = getDimension();
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				dimensionInformation = DungeonManager.getDimensionManagerClient().getDimensionInformation(dim);
			} else {
				// Note: we cannot use world here since we are possibly still busy setting up our world so the 'mapStorage'
				// is always correct here. So we have to use the overworld.
				dimensionInformation = DungeonManager.getDimensionManager(DimensionManager.getWorld(0)).getDimensionInformation(dim);
			}
			if (dimensionInformation == null) {
				Carpetbag.getLogger().catching(new RuntimeException("Dimension information for dimension " + dim + " is missing!"));
			} else {
				setSeed(dim);
//                setupProviderInfo();
			}
		}
		return dimensionInformation;
	}

	@Override
	public DimensionType getDimensionType() {
		return ModDimensions.dungeonType;
		//return DIMENSION_ID == ModConfiguration.DIMENSION_PROVIDER_ID ? ModDimensions.dungeonType : ModDimensions.dungeonTypes.get(ID - ModConfiguration.DIMENSION_PROVIDER_ID);
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		// TODO: Add dungeon generation
		return new ChunkGeneratorFlat(world, seed, false, null);
		//return new ChunkGeneratorNetherTemplate(world, true, world.getSeed());
	}

	@Override
	public boolean canRespawnHere()
	{
		// TODO: Maybe want respawns in the dungeon? idk
		return false;
	}

	@Override
	public String getSaveFolder() {
		return "CARPETBAG" + getDimension();
		//return "DIM" + DIMENSION_ID + "/" + ID;
	}

	@Override
	protected void init() {
		super.init();
		if (world instanceof WorldServer) {
			createBiomeProviderInternal();
			return;
		}

		// We are on a client here and we don't have sufficient information right here (dimension information has not synced yet)
		biomeProvider = null;
	}

	private void setSeed(int dim) {
		if (dimensionInformation == null) {
			if (world == null) {
				return;
			}
			dimensionInformation = DungeonManager.getDimensionManager(world).getDimensionInformation(dim);
			if (dimensionInformation == null) {
				Carpetbag.getLogger().error("Error: setSeed() called with null diminfo. Error ignored!");
				return;
			}
		}
		long forcedSeed = dimensionInformation.getForcedDimensionSeed();
		if (forcedSeed != 0) {
			Carpetbag.getLogger().info("Forced seed for dimension " + dim + ": " + forcedSeed);
			seed = forcedSeed;
		} else {
			long baseSeed = dimensionInformation.getBaseSeed();
			if (baseSeed != 0) {
				seed = calculateSeed(baseSeed, dim) ;
			} else {
				seed = calculateSeed(world.getSeed(), dim) ;
			}
		}
//        seed = dimensionInformation.getBaseSeed();
//        System.out.println("seed = " + seed);
	}

	private DimensionStorage getStorage() {
		if (storage == null) {
			storage = DimensionStorage.getDimensionStorage(world);
		}
		return storage;
	}

	private void createBiomeProviderInternal() {
		getDimensionInformation();
		if (dimensionInformation != null) {
			WorldInfo worldInfo = world.getWorldInfo();
			worldInfo = new WorldInfo(worldInfo) {
				@Override
				public long getSeed() {
					return seed;
				}
			};
			this.biomeProvider = new BiomeProvider(worldInfo);
		} else {
			this.biomeProvider = new BiomeProvider(world.getWorldInfo());
		}

		if (dimensionInformation != null) {
			//this.hasSkyLight = dimensionInformation.getTerrainType().hasSky();
			//setupSkyRenderers();
		}
	}
}
