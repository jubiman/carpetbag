package us.jusybiberman.carpetbag.dungeons;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.dungeons.description.*;
//import us.jusybiberman.carpetbag.dungeons.types.*;
import us.jusybiberman.carpetbag.dungeons.description.DimensionDescriptor;
import us.jusybiberman.carpetbag.util.BlockPosTools;
import us.jusybiberman.carpetbag.util.GenericTools;
import us.jusybiberman.carpetbag.util.ModConfiguration;
import us.jusybiberman.carpetbag.util.network.NetworkTools;

import java.util.*;

public class DimensionInformation {
    private final DimensionDescriptor descriptor;
    private String name;
    private BlockPos spawnPoint = null;

    private int probeCounter = 0;

    private long forcedDimensionSeed = 0;               // Seed was forced using a seed dimlet.
    private long baseSeed = 0;                          // World seed we start to generate own seed from.

    private int worldVersion = VERSION_OLD;             // Used for compatilibity checking between generated worlds.
    public static final int VERSION_OLD = 0;            // Old version of worlds. Seed is incorrect.
    public static final int VERSION_CORRECTSEED = 1;    // New version of worlds. Seed is correct.
    public static final int VERSION_DIMLETSSEED = 2;    // Newer version of worlds. Seed is correct and based only on the world seed and dimlets.

    //private TerrainType terrainType = TerrainType.TERRAIN_VOID;
    private IBlockState baseBlockForTerrain = null;
    private Block fluidForTerrain = null;
    private IBlockState tendrilBlock = null;
    private IBlockState canyonBlock = null;
    private IBlockState[] pyramidBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private IBlockState[] sphereBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private IBlockState[] hugeSphereBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private IBlockState[] scatteredSphereBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private IBlockState[] liquidSphereBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private Block[] liquidSphereFluids = new Block[] { Blocks.WATER };
    private IBlockState[] hugeLiquidSphereBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
    private Block[] hugeLiquidSphereFluids = new Block[] { Blocks.WATER };
    private IBlockState[] extraOregen = new IBlockState[] {};
    private Block[] fluidsForLakes = new Block[] {};

    private List<MobDescriptor> extraMobs = new ArrayList<>();
    private boolean noanimals = false;
    private boolean shelter = false;
    private boolean respawnHere = false;
    private boolean cheater = false;
    private boolean active = true;

    //private Set<FeatureType> featureTypes = EnumSet.noneOf(FeatureType.class);
    //private Set<StructureType> structureTypes = EnumSet.noneOf(StructureType.class);
    //private Set<EffectType> effectTypes = EnumSet.noneOf(EffectType.class);

    //private ControllerType controllerType = null;
    private final List<Biome> biomes = new ArrayList<>();
    private final Map<Integer, Integer> biomeMapping = new HashMap<>();

    private String digitString = "";
    private Float timeSpeed = null;
    private String[] dimensionTypes = new String[0];    // Used for Recurrent Complex if that's present.

    public DimensionInformation(String name, DimensionDescriptor descriptor, World world) {
        this.name = name;
        this.descriptor = descriptor;

        this.forcedDimensionSeed = descriptor.getForcedSeed();
        long overworldSeed = DungeonManager.getWorldForDimension(world, 0).getSeed();
        if (ModConfiguration.randomizeSeed) {
            baseSeed = (long) (Math.random() * 10000 + 1);
        } else {
            baseSeed = overworldSeed;
        }

        worldVersion = VERSION_DIMLETSSEED;

        setupFromDescriptor(overworldSeed);
        setupBiomeMapping();

        dump(null);
    }

    private void setupFromDescriptor(long seed) {

    }


    public DimensionInformation(DimensionDescriptor descriptor, NBTTagCompound tagCompound) {
        this.name = tagCompound.getString("name");
        this.descriptor = descriptor;

        //setSpawnPoint(BlockPosTools.readFromNBT(tagCompound, "spawnPoint"));
        this.probeCounter = tagCompound.getInteger("probeCounter");

        int version = tagCompound.getInteger("version");
        if (version == 1) {
            // This version of the dimension information has the random information persisted.
            setupFromNBT(tagCompound);
        } else {
            // This is an older version. Here we have to calculate the random information again.
            setupFromDescriptor(1);
        }

        setupBiomeMapping();
    }

    public static int[] getIntArraySafe(NBTTagCompound tagCompound, String name) {
        if (!tagCompound.hasKey(name)) {
            return new int[0];
        }
        NBTBase tag = tagCompound.getTag(name);
        if (tag instanceof NBTTagIntArray) {
            return tagCompound.getIntArray(name);
        } else {
            return new int[0];
        }
    }

    private void setupFromNBT(NBTTagCompound tagCompound) {
        /*
        terrainType = TerrainType.values()[tagCompound.getInteger("terrain")];
        featureTypes = toEnumSet(getIntArraySafe(tagCompound, "features"), FeatureType.values());
        structureTypes = toEnumSet(getIntArraySafe(tagCompound, "structures"), StructureType.values());
        effectTypes = toEnumSet(getIntArraySafe(tagCompound, "effects"), EffectType.values());
        */

        biomes.clear();
        for (int a : getIntArraySafe(tagCompound, "biomes")) {
            Biome biome = Biome.getBiome(a);
            if (biome != null) {
                biomes.add(biome);
            } else {
                // Protect against deleted biomes (i.e. a mod with biomes gets removed and this dimension still uses it).
                // We will pick a replacement biome here.
                biomes.add(Biomes.PLAINS);
            }
        }
        /*if (tagCompound.hasKey("controller")) {
            controllerType = ControllerType.values()[tagCompound.getInteger("controller")];
        } else {
            // Support for old type.
            if (biomes.isEmpty()) {
                controllerType = ControllerType.CONTROLLER_DEFAULT;
            } else {
                controllerType = ControllerType.CONTROLLER_SINGLE;
            }
        }*/

        digitString = tagCompound.getString("digits");

        forcedDimensionSeed = tagCompound.getLong("forcedSeed");
        baseSeed = tagCompound.getLong("baseSeed");
        worldVersion = tagCompound.getInteger("worldVersion");

        baseBlockForTerrain = getBlockMeta(tagCompound, "baseBlock");
        tendrilBlock = getBlockMeta(tagCompound, "tendrilBlock");
        canyonBlock = getBlockMeta(tagCompound, "canyonBlock");
        fluidForTerrain = Block.REGISTRY.getObjectById(tagCompound.getInteger("fluidBlock"));

        hugeLiquidSphereFluids = readFluidsFromNBT(tagCompound, "hugeLiquidSphereFluids");
        hugeLiquidSphereBlocks = readBlockArrayFromNBT(tagCompound, "hugeLiquidSphereBlocks");

        // Support for the old format with only one liquid block.
        Block oldLiquidSphereFluid = Block.REGISTRY.getObjectById(tagCompound.getInteger("liquidSphereFluid"));
        liquidSphereFluids = readFluidsFromNBT(tagCompound, "liquidSphereFluids");
        if (liquidSphereFluids.length == 0) {
            liquidSphereFluids = new Block[] { oldLiquidSphereFluid };
        }

        // Support for the old format with only one sphere block.
        IBlockState oldLiquidSphereBlock = getBlockMeta(tagCompound, "liquidSphereBlock");
        liquidSphereBlocks = readBlockArrayFromNBT(tagCompound, "liquidSphereBlocks");
        if (liquidSphereBlocks.length == 0) {
            liquidSphereBlocks = new IBlockState[] { oldLiquidSphereBlock };
        }

        pyramidBlocks = readBlockArrayFromNBT(tagCompound, "pyramidBlocks");
        if (pyramidBlocks.length == 0) {
            pyramidBlocks = new IBlockState[] { Blocks.STONE.getDefaultState() };
        }

        // Support for the old format with only one sphere block.
        IBlockState oldSphereBlock = getBlockMeta(tagCompound, "sphereBlock");
        sphereBlocks = readBlockArrayFromNBT(tagCompound, "sphereBlocks");
        if (sphereBlocks.length == 0) {
            sphereBlocks = new IBlockState[] { oldSphereBlock };
        }

        hugeSphereBlocks = readBlockArrayFromNBT(tagCompound, "hugeSphereBlocks");
        scatteredSphereBlocks = readBlockArrayFromNBT(tagCompound, "scatteredSphereBlocks");

        extraOregen = readBlockArrayFromNBT(tagCompound, "extraOregen");
        fluidsForLakes = readFluidsFromNBT(tagCompound, "lakeFluids");

        noanimals = tagCompound.getBoolean("noanimals");
        shelter = tagCompound.getBoolean("shelter");
        respawnHere = tagCompound.getBoolean("respawnHere");
        cheater = tagCompound.getBoolean("cheater");

        if (tagCompound.hasKey("timeSpeed")) {
            timeSpeed = tagCompound.getFloat("timeSpeed");
        } else {
            timeSpeed = null;
        }
        probeCounter = tagCompound.getInteger("probes");

        extraMobs.clear();
        NBTTagList list = tagCompound.getTagList("mobs", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagCompound tc = list.getCompoundTagAt(i);
            String className = tc.getString("class");
            int chance = tc.getInteger("chance");
            int minGroup = tc.getInteger("minGroup");
            int maxGroup = tc.getInteger("maxGroup");
            int maxLoaded = tc.getInteger("maxLoaded");
            Class<? extends EntityLiving> c = null;
            try {
                c = GenericTools.castClass(Class.forName(className), EntityLiving.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            MobDescriptor mob = new MobDescriptor(c, chance, minGroup, maxGroup, maxLoaded);
            extraMobs.add(mob);
        }

        String ds = tagCompound.getString("dimensionTypes");
        dimensionTypes = StringUtils.split(ds, ",");
        if (dimensionTypes == null) {
            dimensionTypes = new String[0];
        }
    }

    private Block[] readFluidsFromNBT(NBTTagCompound tagCompound, String name) {
        List<Block> fluids = new ArrayList<>();

        if (tagCompound.hasKey(name + "_reg")) {
            // New system
            NBTTagList list = tagCompound.getTagList(name + "_reg", Constants.NBT.TAG_STRING);
            for (int i = 0 ; i < list.tagCount() ; i++) {
                NBTTagString reg = (NBTTagString) list.get(i);
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(reg.getString()));
                if (block == null) {
                    // Block no longer exists, ignore
                    Carpetbag.getLogger().error("Block with id " + reg.getString() + " no longer exists! Ignoring...");
                } else {
                    fluids.add(block);
                }
            }
        } else {
            // Old system
            for (int a : getIntArraySafe(tagCompound, name)) {
                fluids.add(Block.REGISTRY.getObjectById(a));
            }
        }
        return fluids.toArray(new Block[fluids.size()]);
    }

    private static IBlockState[] readBlockArrayFromNBT(NBTTagCompound tagCompound, String name) {
        List<IBlockState> blocks = new ArrayList<>();
        int[] metas = getIntArraySafe(tagCompound, name + "_meta");

        if (tagCompound.hasKey(name + "_reg")) {
            // New system
            NBTTagList list = tagCompound.getTagList(name + "_reg", Constants.NBT.TAG_STRING);
            for (int i = 0 ; i < list.tagCount() ; i++) {
                NBTTagString reg = (NBTTagString) list.get(i);
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(reg.getString()));
                if (block == null) {
                    // Block no longer exists, ignore
                    Carpetbag.getLogger().error("Block with id " + reg.getString() + " no longer exists! Ignoring...");
                } else {
                    int meta = 0;
                    if (i < metas.length) {
                        meta = metas[i];
                    }
                    try {
                        blocks.add(block.getStateFromMeta(meta));
                    } catch (Exception e) {
                        Carpetbag.getLogger().error("Block with id " + reg.getString() + " no longer supports meta " + meta + "! Ignoring...");
                    }
                }
            }
        } else {
            // Old system
            int[] blockIds = getIntArraySafe(tagCompound, name);
            for (int i = 0; i < blockIds.length; i++) {
                int id = blockIds[i];
                Block block = Block.REGISTRY.getObjectById(id);
                int meta = 0;
                if (i < metas.length) {
                    meta = metas[i];
                }
                try {
                    blocks.add(block.getStateFromMeta(meta));
                } catch (Exception e) {
                    // To work around a problem with changed ids we catch this exception here.
                    // In future this should never happen since we don't persist with ids anymore.
                    // But we were not always as smart as we are now...
                    Carpetbag.getLogger().error("Block has changed in dimension: ignoring");
                }
            }
        }
        return blocks.toArray(new IBlockState[blocks.size()]);
    }

    private IBlockState getBlockMeta(NBTTagCompound tagCompound, String name) {
        Block block = Block.REGISTRY.getObjectById(tagCompound.getInteger(name));
        int meta = tagCompound.getInteger(name + "_meta");
        return block.getStateFromMeta(meta);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("name", getName());

        BlockPos spawnPoint = getSpawnPoint();
        if (spawnPoint != null) {
            BlockPosTools.writeToNBT(tagCompound, "spawnPoint", spawnPoint);
        }
        tagCompound.setInteger("probeCounter", getProbeCounter());
        tagCompound.setInteger("version", 1);           // Version number so that we can detect incompatible changes in persisted dimension information objects.

        /*
        tagCompound.setInteger("terrain", terrainType == null ? TerrainType.TERRAIN_VOID.ordinal() : terrainType.ordinal());
        tagCompound.setIntArray("features", toIntArray(featureTypes));
        tagCompound.setIntArray("structures", toIntArray(structureTypes));
        tagCompound.setIntArray("effects", toIntArray(effectTypes));
        */

        List<Integer> c = new ArrayList<>(biomes.size());
        for (Biome t : biomes) {
            if (t != null) {
                c.add(Biome.getIdForBiome(t));
            } else {
                c.add(Biome.getIdForBiome(Biomes.PLAINS));
            }
        }
        tagCompound.setIntArray("biomes", ArrayUtils.toPrimitive(c.toArray(new Integer[c.size()])));
        //tagCompound.setInteger("controller", controllerType == null ? ControllerType.CONTROLLER_DEFAULT.ordinal() : controllerType.ordinal());
        tagCompound.setString("digits", digitString);

        tagCompound.setLong("forcedSeed", forcedDimensionSeed);
        tagCompound.setLong("baseSeed", baseSeed);
        tagCompound.setInteger("worldVersion", worldVersion);

        setBlockMeta(tagCompound, baseBlockForTerrain, "baseBlock");
        setBlockMeta(tagCompound, tendrilBlock, "tendrilBlock");

        writeBlocksToNBT(tagCompound, pyramidBlocks, "pyramidBlocks");

        writeBlocksToNBT(tagCompound, sphereBlocks, "sphereBlocks");
        if (sphereBlocks.length > 0) {
            // Write out a single sphere block for compatibility with older RFTools.
            setBlockMeta(tagCompound, sphereBlocks[0], "sphereBlock");
        }

        writeBlocksToNBT(tagCompound, hugeSphereBlocks, "hugeSphereBlocks");
        writeBlocksToNBT(tagCompound, scatteredSphereBlocks, "scatteredSphereBlocks");
        writeBlocksToNBT(tagCompound, hugeLiquidSphereBlocks, "hugeLiquidSphereBlocks");
        writeFluidsToNBT(tagCompound, hugeLiquidSphereFluids, "hugeLiquidSphereFluids");

        writeBlocksToNBT(tagCompound, liquidSphereBlocks, "liquidSphereBlocks");
        if (liquidSphereBlocks.length > 0) {
            // Write out a single sphere block for compatibility with older RFTools.
            setBlockMeta(tagCompound, liquidSphereBlocks[0], "liquidSphereBlock");
        }

        writeFluidsToNBT(tagCompound, liquidSphereFluids, "liquidSphereFluids");
        if (liquidSphereFluids.length > 0) {
            tagCompound.setInteger("liquidSphereFluid", Block.REGISTRY.getIDForObject(liquidSphereFluids[0]));
        }

        setBlockMeta(tagCompound, canyonBlock, "canyonBlock");
        tagCompound.setInteger("fluidBlock", Block.REGISTRY.getIDForObject(fluidForTerrain));

        writeBlocksToNBT(tagCompound, extraOregen, "extraOregen");
        writeFluidsToNBT(tagCompound, fluidsForLakes, "lakeFluids");

        tagCompound.setBoolean("noanimals", noanimals);
        tagCompound.setBoolean("shelter", shelter);
        tagCompound.setBoolean("respawnHere", respawnHere);
        tagCompound.setBoolean("cheater", cheater);

        if (timeSpeed != null) {
            tagCompound.setFloat("timeSpeed", timeSpeed);
        }
        tagCompound.setInteger("probes", probeCounter);

        NBTTagList list = new NBTTagList();
        for (MobDescriptor mob : extraMobs) {
            NBTTagCompound tc = new NBTTagCompound();

            if (mob != null) {
                tc.setString("class", mob.entityClass.getName());
                tc.setInteger("chance", mob.itemWeight);
                tc.setInteger("minGroup", mob.minGroupCount);
                tc.setInteger("maxGroup", mob.maxGroupCount);
                tc.setInteger("maxLoaded", mob.getMaxLoaded());
                list.appendTag(tc);
            }
        }

        tagCompound.setTag("mobs", list);
        tagCompound.setString("dimensionTypes", StringUtils.join(dimensionTypes, ","));
    }

    private void setBlockMeta(NBTTagCompound tagCompound, IBlockState blockMeta, String name) {
        tagCompound.setInteger(name, Block.REGISTRY.getIDForObject(blockMeta.getBlock()));
        // @todo check if this is the good way to persist?
        tagCompound.setInteger(name + "_meta", blockMeta.getBlock().getMetaFromState(blockMeta));
    }

    private static void writeFluidsToNBT(NBTTagCompound tagCompound, Block[] fluids, String name) {
        NBTTagList list = new NBTTagList();
//        List<Integer> c;
//        c = new ArrayList<Integer>(fluids.length);
        for (Block t : fluids) {
//            c.add(Block.REGISTRY.getIDForObject(t));
            list.appendTag(new NBTTagString(t.getRegistryName().toString()));
        }
//        tagCompound.setIntArray(name, ArrayUtils.toPrimitive(c.toArray(new Integer[c.size()])));
        tagCompound.setTag(name + "_reg", list);
    }

    private static void writeBlocksToNBT(NBTTagCompound tagCompound, IBlockState[] blocks, String name) {
        NBTTagList list = new NBTTagList();
//        List<Integer> ids = new ArrayList<Integer>(blocks.length);
        List<Integer> meta = new ArrayList<>(blocks.length);
        for (IBlockState t : blocks) {
//            ids.add(Block.REGISTRY.getIDForObject(t.getBlock()));
            list.appendTag(new NBTTagString(t.getBlock().getRegistryName().toString()));
            meta.add(t.getBlock().getMetaFromState(t));
        }
//        tagCompound.setIntArray(name, ArrayUtils.toPrimitive(ids.toArray(new Integer[ids.size()])));

        tagCompound.setTag(name + "_reg", list);
        tagCompound.setIntArray(name + "_meta", ArrayUtils.toPrimitive(meta.toArray(new Integer[meta.size()])));
    }

    private static <T extends Enum<T>> int[] toIntArray(Collection<T> collection) {
        List<Integer> c = new ArrayList<>(collection.size());
        for (T t : collection) {
            c.add(t.ordinal());
        }
        return ArrayUtils.toPrimitive(c.toArray(new Integer[c.size()]));
    }

    private static <T extends Enum<T>> Set<T> toEnumSet(int[] arr, T[] values) {
        Set<T> list = new HashSet<>();
        for (int a : arr) {
            list.add(values[a]);
        }
        return list;
    }


    private void logDebug(EntityPlayer player, String message) {
        if (player == null) {
            Carpetbag.getLogger().info(message);
        } else {
            player.sendMessage( new TextComponentString(TextFormatting.YELLOW + message));
        }
    }

    public static String getDisplayName(Block block) {
        if (block == null) {
            return "null";
        }

        return getDisplayName(block.getDefaultState());
    }

    public static String getDisplayName(IBlockState state) {
        if (state == null) {
            return "null";
        }

        Block block = state.getBlock();

        String suffix = "";

        if (block instanceof BlockLiquid) {
            Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
            if (fluid != null) {
                FluidStack stack = new FluidStack(fluid, 1);
                return stack.getLocalizedName() + suffix;
            }
        }

        Item item = Item.getItemFromBlock(block);
        ItemStack itemStack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(state) : 0);
        if (itemStack.isEmpty()) {
            return block.getLocalizedName() + suffix;
        }

        return itemStack.getDisplayName() + suffix;
    }

    public void dump(EntityPlayer player) {
        String digits = getDigitString();
        if (!digits.isEmpty()) {
            logDebug(player, "    Digits: " + digits);
        }
        if (forcedDimensionSeed != 0) {
            logDebug(player, "    Forced seed: " + forcedDimensionSeed);
        }
        if (baseSeed != 0) {
            logDebug(player, "    Base seed: " + baseSeed);
        }
        logDebug(player, "    World version: " + worldVersion);
        //TerrainType terrainType = getTerrainType();
        //logDebug(player, "    Terrain: " + terrainType.toString());
        logDebug(player, "        Base block: " + getDisplayName(baseBlockForTerrain));
        /*
        if (featureTypes.contains(FeatureType.FEATURE_TENDRILS)) {
            logDebug(player, "        Tendril block: " + getDisplayName(tendrilBlock));
        }
        if (featureTypes.contains(FeatureType.FEATURE_PYRAMIDS)) {
            for (IBlockState block : pyramidBlocks) {
                if (block != null) {
                    logDebug(player, "        Pyramid blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_ORBS)) {
            for (IBlockState block : sphereBlocks) {
                if (block != null) {
                    logDebug(player, "        Orb blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_HUGEORBS)) {
            for (IBlockState block : hugeSphereBlocks) {
                if (block != null) {
                    logDebug(player, "        Huge Orb blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_SCATTEREDORBS)) {
            for (IBlockState block : scatteredSphereBlocks) {
                if (block != null) {
                    logDebug(player, "        Scattered Orb blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_LIQUIDORBS)) {
            for (IBlockState block : liquidSphereBlocks) {
                if (block != null) {
                    logDebug(player, "        Liquid Orb blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_HUGELIQUIDORBS)) {
            for (IBlockState block : hugeLiquidSphereBlocks) {
                if (block != null) {
                    logDebug(player, "        Huge Liquid Orb blocks: " + getDisplayName(block));
                }
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_CANYONS)) {
            logDebug(player, "        Canyon block: " + getDisplayName(canyonBlock));
        }
        */
        logDebug(player, "        Base fluid: " + getDisplayName(fluidForTerrain));
        //logDebug(player, "    Biome controller: " + (controllerType == null ? "<null>" : controllerType.name()));
        for (Biome biome : getBiomes()) {
            if (biome != null) {
                logDebug(player, "    Biome: " + biome.getBiomeName());
            }
        }
        /*for (FeatureType featureType : getFeatureTypes()) {
            logDebug(player, "    Feature: " + featureType.toString());
        }*/
        for (IBlockState block : extraOregen) {
            if (block != null) {
                logDebug(player, "        Extra ore: " + getDisplayName(block));
            }
        }
        for (Block block : fluidsForLakes) {
            logDebug(player, "        Lake fluid: " + getDisplayName(block));
        }
        /*if (featureTypes.contains(FeatureType.FEATURE_LIQUIDORBS)) {
            for (Block fluid : liquidSphereFluids) {
                logDebug(player, "        Liquid orb fluids: " + getDisplayName(fluid));
            }
        }
        if (featureTypes.contains(FeatureType.FEATURE_HUGELIQUIDORBS)) {
            for (Block fluid : hugeLiquidSphereFluids) {
                logDebug(player, "        Huge Liquid orb fluids: " + getDisplayName(fluid));
            }
        }
        for (StructureType structureType : getStructureTypes()) {
            logDebug(player, "    Structure: " + structureType.toString());
        }
//        if (structureTypes.contains(StructureType.STRUCTURE_RECURRENTCOMPLEX)) {
//            for (String type : dimensionTypes) {
//                logDebug(player, "    RR DimensionType: " + type);
//            }
//        }
        for (EffectType effectType : getEffectTypes()) {
            logDebug(player, "    Effect: " + effectType.toString());
        }
        logDebug(player, "    Sun brightness: " + skyDescriptor.getSunBrightnessFactor());
        logDebug(player, "    Star brightness: " + skyDescriptor.getStarBrightnessFactor());
        float r = skyDescriptor.getSkyColorFactorR();
        float g = skyDescriptor.getSkyColorFactorG();
        float b = skyDescriptor.getSkyColorFactorB();
        logDebug(player, "    Sky color: " + r + ", " + g + ", " + b);
        r = skyDescriptor.getFogColorFactorR();
        g = skyDescriptor.getFogColorFactorG();
        b = skyDescriptor.getFogColorFactorB();
        logDebug(player, "    Fog color: " + r + ", " + g + ", " + b);
        r = skyDescriptor.getCloudColorFactorR();
        g = skyDescriptor.getCloudColorFactorG();
        b = skyDescriptor.getCloudColorFactorB();
        logDebug(player, "    Cloud color: " + r + ", " + g + ", " + b);
        SkyType skyType = skyDescriptor.getSkyType();
        if (skyType != SkyType.SKY_NORMAL) {
            logDebug(player, "    Sky type: " + skyType.toString());
        }
        for (CelestialBodyType bodyType : skyDescriptor.getCelestialBodies()) {
            logDebug(player, "    Sky body: " + bodyType.name());
        }
        */


        for (MobDescriptor mob : extraMobs) {
            if (mob != null) {
                logDebug(player, "    Mob: " + mob.entityClass.getName());
            }
        }
        if (noanimals) {
            logDebug(player, "    No animals mode");
        }
        if (shelter) {
            logDebug(player, "    Safe shelter");
        }
        if (respawnHere) {
            logDebug(player, "    Respawn local");
        }
        if (cheater) {
            logDebug(player, "    CHEATER!");
        }
        if (timeSpeed != null) {
            logDebug(player, "    Time speed: " + timeSpeed);
        }
        if (probeCounter > 0) {
            logDebug(player, "    Probes: " + probeCounter);
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(biomes.size());
        for (Biome entry : biomes) {
            if (entry != null) {
                int id = Biome.getIdForBiome(entry);
                buf.writeInt(id);
            } else {
                buf.writeInt(Biome.getIdForBiome(Biomes.PLAINS));
            }
        }

        NetworkTools.writeString(buf, digitString);
        buf.writeLong(forcedDimensionSeed);
        buf.writeLong(baseSeed);
        buf.writeInt(worldVersion);

        buf.writeInt(Block.REGISTRY.getIDForObject(baseBlockForTerrain.getBlock()));
        buf.writeInt(baseBlockForTerrain.getBlock().getMetaFromState(baseBlockForTerrain));
        buf.writeInt(Block.REGISTRY.getIDForObject(tendrilBlock.getBlock()));
        buf.writeInt(tendrilBlock.getBlock().getMetaFromState(tendrilBlock));

        writeBlockArrayToBuf(buf, pyramidBlocks);
        writeBlockArrayToBuf(buf, sphereBlocks);
        writeBlockArrayToBuf(buf, hugeSphereBlocks);
        writeBlockArrayToBuf(buf, scatteredSphereBlocks);
        writeBlockArrayToBuf(buf, liquidSphereBlocks);
        writeFluidArrayToBuf(buf, liquidSphereFluids);
        writeBlockArrayToBuf(buf, hugeLiquidSphereBlocks);
        writeFluidArrayToBuf(buf, hugeLiquidSphereFluids);

        buf.writeInt(Block.REGISTRY.getIDForObject(canyonBlock.getBlock()));
        buf.writeInt(canyonBlock.getBlock().getMetaFromState(canyonBlock));
        buf.writeInt(Block.REGISTRY.getIDForObject(fluidForTerrain));

        writeBlockArrayToBuf(buf, extraOregen);

        writeFluidArrayToBuf(buf, fluidsForLakes);


        buf.writeBoolean(noanimals);
        buf.writeBoolean(shelter);
        buf.writeBoolean(respawnHere);
        buf.writeBoolean(cheater);
        NetworkTools.writeFloat(buf, timeSpeed);

        buf.writeInt(probeCounter);


        int mobsize = 0;
        for (MobDescriptor mob : extraMobs) {
            if (mob != null) {
                mobsize++;
            }
        }
        buf.writeInt(mobsize);
        for (MobDescriptor mob : extraMobs) {
            if (mob != null) {
                NetworkTools.writeString(buf, mob.entityClass.getName());
                buf.writeInt(mob.itemWeight);
                buf.writeInt(mob.minGroupCount);
                buf.writeInt(mob.maxGroupCount);
                buf.writeInt(mob.getMaxLoaded());
            }
        }

        buf.writeInt(dimensionTypes.length);
        for (String type : dimensionTypes) {
            NetworkTools.writeString(buf, type);
        }

    }

    private static void writeFluidArrayToBuf(ByteBuf buf, Block[] fluids) {
        buf.writeInt(fluids.length);
        for (Block block : fluids) {
            buf.writeInt(Block.REGISTRY.getIDForObject(block));
        }
    }

    private static void writeBlockArrayToBuf(ByteBuf buf, IBlockState[] array) {
        buf.writeInt(array.length);
        for (IBlockState block : array) {
            buf.writeInt(Block.REGISTRY.getIDForObject(block.getBlock()));
            buf.writeInt(block.getBlock().getMetaFromState(block));
        }
    }

    public DimensionInformation(String name, DimensionDescriptor descriptor, ByteBuf buf) {
        this.name = name;
        this.descriptor = descriptor;

        biomes.clear();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            int id = buf.readInt();
            Biome biome = Biome.getBiome(id);
            if (biome != null) {
                biomes.add(biome);
            } else {
                biomes.add(Biomes.PLAINS);
            }
        }
        digitString = NetworkTools.readString(buf);

        forcedDimensionSeed = buf.readLong();
        baseSeed = buf.readLong();
        worldVersion = buf.readInt();

        Block block = Block.REGISTRY.getObjectById(buf.readInt());
        int meta = buf.readInt();
        baseBlockForTerrain = block.getStateFromMeta(meta);
        block = Block.REGISTRY.getObjectById(buf.readInt());
        meta = buf.readInt();
        tendrilBlock = block.getStateFromMeta(meta);

        pyramidBlocks = readBlockArrayFromBuf(buf);
        sphereBlocks = readBlockArrayFromBuf(buf);
        hugeSphereBlocks = readBlockArrayFromBuf(buf);
        scatteredSphereBlocks = readBlockArrayFromBuf(buf);
        liquidSphereBlocks = readBlockArrayFromBuf(buf);
        liquidSphereFluids = readFluidArrayFromBuf(buf);
        hugeLiquidSphereBlocks = readBlockArrayFromBuf(buf);
        hugeLiquidSphereFluids = readFluidArrayFromBuf(buf);

        block = Block.REGISTRY.getObjectById(buf.readInt());
        meta = buf.readInt();
        canyonBlock = block.getStateFromMeta(meta);
        fluidForTerrain = Block.REGISTRY.getObjectById(buf.readInt());

        extraOregen = readBlockArrayFromBuf(buf);

        fluidsForLakes = readFluidArrayFromBuf(buf);

        noanimals = buf.readBoolean();
        shelter = buf.readBoolean();
        respawnHere = buf.readBoolean();
        cheater = buf.readBoolean();

        timeSpeed = NetworkTools.readFloat(buf);

        probeCounter = buf.readInt();

        extraMobs.clear();
        size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            String className = NetworkTools.readString(buf);
            int chance = buf.readInt();
            int minGroup = buf.readInt();
            int maxGroup = buf.readInt();
            int maxLoaded = buf.readInt();

            try {
                Class<? extends EntityLiving> c = GenericTools.castClass(Class.forName(className), EntityLiving.class);
                MobDescriptor mob = new MobDescriptor(c, chance, minGroup, maxGroup, maxLoaded);
                extraMobs.add(mob);
            } catch (ClassNotFoundException e) {
                Carpetbag.getLogger().error("Cannot find class: " + className + "!", e);
            }
        }

        size = buf.readInt();
        dimensionTypes = new String[size];
        for (int i = 0 ; i < size ; i++) {
            dimensionTypes[i] = NetworkTools.readString(buf);
        }

        setupBiomeMapping();
    }

    private static Block[] readFluidArrayFromBuf(ByteBuf buf) {
        List<Block> blocks = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            blocks.add(Block.REGISTRY.getObjectById(buf.readInt()));
        }
        return blocks.toArray(new Block[blocks.size()]);
    }

    private static IBlockState[] readBlockArrayFromBuf(ByteBuf buf) {
        int size = buf.readInt();
        List<IBlockState> blocksMeta = new ArrayList<>();
        for (int i = 0 ; i < size ; i++) {
            Block b = Block.REGISTRY.getObjectById(buf.readInt());
            int m = buf.readInt();
            blocksMeta.add(b.getStateFromMeta(m));
        }
        return blocksMeta.toArray(new IBlockState[blocksMeta.size()]);
    }

    public BlockPos getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(BlockPos spawnPoint) {
        this.spawnPoint = spawnPoint;
    }


    private void setupBiomeMapping() {
        /*biomeMapping.clear();
        if (controllerType == ControllerType.CONTROLLER_FILTERED) {
            final Set<Integer> ids = new HashSet<>();
            for (Biome biome : biomes) {
                if (biome != null) {
                    ids.add(Biome.getIdForBiome(biome));
                } else {
                    ids.add(Biome.getIdForBiome(Biomes.PLAINS));
                }
            }

            ControllerType.BiomeFilter biomeFilter = new ControllerType.BiomeFilter() {
                @Override
                public boolean match(Biome biome) {
                    return ids.contains(Biome.getIdForBiome(biome));
                }

                @Override
                public double calculateBiomeDistance(Biome a, Biome b) {
                    return calculateBiomeDistance(a, b, false, false, false);
                }
            };
            BiomeControllerMapping.makeFilteredBiomeMap(biomeMapping, biomeFilter);
        }*/
    }

    public DimensionDescriptor getDescriptor() {
        return descriptor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    public Map<Integer, Integer> getBiomeMapping() {
        if (biomeMapping.isEmpty()) {
            setupBiomeMapping();
        }
        return biomeMapping;
    }

    public String getDigitString() {
        return digitString;
    }

    public void setDigitString(String digitString) {
        this.digitString = digitString;
    }

    public IBlockState getBaseBlockForTerrain() {
        return baseBlockForTerrain;
    }

    public void setBaseBlockForTerrain(IBlockState blockMeta) { baseBlockForTerrain = blockMeta; }

    public IBlockState getTendrilBlock() {
        return tendrilBlock;
    }

    public void setTendrilBlock(IBlockState block) {
        tendrilBlock = block;
    }

    public IBlockState getCanyonBlock() {
        return canyonBlock;
    }

    public void setCanyonBlock(IBlockState canyonBlock) {
        this.canyonBlock = canyonBlock;
    }

    public IBlockState[] getPyramidBlocks() {
        return pyramidBlocks;
    }

    public void setPyramidBlocks(IBlockState[] pyramidBlocks) {
        this.pyramidBlocks = pyramidBlocks;
    }

    public IBlockState[] getSphereBlocks() {
        return sphereBlocks;
    }

    public void setSphereBlocks(IBlockState[] sphereBlocks) {
        this.sphereBlocks = sphereBlocks;
    }

    public IBlockState[] getHugeSphereBlocks() {
        return hugeSphereBlocks;
    }

    public void setHugeSphereBlocks(IBlockState[] hugeSphereBlocks) {
        this.hugeSphereBlocks = hugeSphereBlocks;
    }

    public void setScatteredSphereBlocks(IBlockState[] scatteredSphereBlocks) {
        this.scatteredSphereBlocks = scatteredSphereBlocks;
    }

    public IBlockState[] getScatteredSphereBlocks() {
        return scatteredSphereBlocks;
    }

    public IBlockState[] getLiquidSphereBlocks() {
        return liquidSphereBlocks;
    }

    public void setLiquidSphereBlocks(IBlockState[] liquidSphereBlocks) {
        this.liquidSphereBlocks = liquidSphereBlocks;
    }

    public Block[] getLiquidSphereFluids() {
        return liquidSphereFluids;
    }

    public void setLiquidSphereFluids(Block[] liquidSphereFluids) {
        this.liquidSphereFluids = liquidSphereFluids;
    }

    public IBlockState[] getHugeLiquidSphereBlocks() {
        return hugeLiquidSphereBlocks;
    }

    public void setHugeLiquidSphereBlocks(IBlockState[] hugeLiquidSphereBlocks) {
        this.hugeLiquidSphereBlocks = hugeLiquidSphereBlocks;
    }

    public Block[] getHugeLiquidSphereFluids() {
        return hugeLiquidSphereFluids;
    }

    public void setHugeLiquidSphereFluids(Block[] hugeLiquidSphereFluids) {
        this.hugeLiquidSphereFluids = hugeLiquidSphereFluids;
    }

    public IBlockState[] getExtraOregen() {
        return extraOregen;
    }

    public void setExtraOregen(IBlockState[] blocks) {
        extraOregen = blocks;
    }

    public Block getFluidForTerrain() {
        return fluidForTerrain;
    }

    public void setFluidForTerrain(Block block) { fluidForTerrain = block; }

    public Block[] getFluidsForLakes() {
        return fluidsForLakes;
    }

    public void setFluidsForLakes(Block[] blocks) {
        fluidsForLakes = blocks;
    }

    public String[] getDimensionTypes() {
        return dimensionTypes;
    }

    public void setDimensionTypes(String[] dimensionTypes) {
        this.dimensionTypes = dimensionTypes;
    }

    public List<MobDescriptor> getExtraMobs() {
        return extraMobs;
    }

    public boolean isNoanimals() {
        return noanimals;
    }

    public void setNoanimals(boolean noanimals) {
        this.noanimals = noanimals;
    }

    public boolean isShelter() {
        return shelter;
    }

    public void setShelter(boolean shelter) {
        this.shelter = shelter;
    }

    public boolean isRespawnHere() {
        return respawnHere;
    }

    public void setRespawnHere(boolean respawnHere) {
        this.respawnHere = respawnHere;
    }

    public boolean isCheater() {
        return cheater;
    }

    public void setCheater(boolean cheater) {
        this.cheater = cheater;
    }

    public Float getTimeSpeed() {
        return timeSpeed;
    }

    public void setTimeSpeed(Float timeSpeed) {
        this.timeSpeed = timeSpeed;
    }

    public void addProbe() {
        probeCounter++;
    }

    public void removeProbe() {
        probeCounter--;
        if (probeCounter < 0) {
            probeCounter = 0;
        }
    }

    public int getProbeCounter() {
        return probeCounter;
    }

    public long getForcedDimensionSeed() {
        return forcedDimensionSeed;
    }

    public long getBaseSeed() {
        return baseSeed;
    }

    public int getWorldVersion() {
        return worldVersion;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean val) {
        active = val;
    }
}
