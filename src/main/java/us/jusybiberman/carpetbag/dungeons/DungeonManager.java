package us.jusybiberman.carpetbag.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.dungeons.description.DimensionDescriptor;
import us.jusybiberman.carpetbag.util.ModConfiguration;

import java.io.File;
import java.util.*;

public class DungeonManager extends WorldSavedData {
	public static final String DIMMANAGER_NAME = "CarpetbagDungeonManager";
	private static DungeonManager instance = null;
	private static DungeonManager clientInstance = null;

	private final Map<Integer, DimensionDescriptor> dimensions = new HashMap<>();
	private final Map<DimensionDescriptor, Integer> dimensionToID = new HashMap<>();
	private final Map<Integer, DimensionInformation> dimensionInformation = new HashMap<>();
	private final Set<Integer> reclaimedIds = new HashSet<>();

	@SideOnly(Side.CLIENT)
	public void syncFromServer(Map<Integer, DimensionDescriptor> dims, Map<Integer, DimensionInformation> dimInfo) {
		for (Map.Entry<Integer, DimensionDescriptor> entry : dims.entrySet()) {
			int id = entry.getKey();
			DimensionDescriptor descriptor = entry.getValue();
			if (dimensions.containsKey(id)) {
				dimensionToID.remove(dimensions.get(id));
			}
			dimensions.put(id, descriptor);
			dimensionToID.put(descriptor, id);
		}

		WorldClient world = Minecraft.getMinecraft().world;
		Dungeon provider = (world != null && world.provider instanceof Dungeon) ? (Dungeon) world.provider : null;
		for (Map.Entry<Integer, DimensionInformation> entry : dimInfo.entrySet()) {
			int id = entry.getKey();
			DimensionInformation info = entry.getValue();
			dimensionInformation.put(id, info);
			if (provider != null && provider.getDimension() == id) {
				provider.setDimensionInformation(info);
			}
		}
	}

	public DungeonManager(String identifier) {
		super(identifier);
	}

	public DungeonManager() {
		super(DIMMANAGER_NAME);
	}

	// -----------------------------------------------
	// -----------------------------------------------
	// -----------------------------------------------

	
	public static void clearInstance() {
		if (instance != null) {
			instance.dimensions.clear();
			instance.dimensionToID.clear();
			instance.dimensionInformation.clear();
			instance.reclaimedIds.clear();
			instance = null;
		}
	}

	public static void cleanupDimensionInformation() {
		if (instance != null) {
			Carpetbag.getLogger().info("Cleaning up Carpetbag dimensions");
			unregisterDimensions();
			instance.getDimensions().clear();
			instance.dimensionToID.clear();
			instance.dimensionInformation.clear();
			instance.reclaimedIds.clear();
			instance = null;
		}
	}

	public static void unregisterDimensions() {
		for (Map.Entry<Integer, DimensionDescriptor> me : instance.getDimensions().entrySet()) {
			int id = me.getKey();
			if (DimensionManager.isDimensionRegistered(id)) {
				Carpetbag.getLogger().info("    Unregister dimension: " + id);
				try {
					DimensionManager.unregisterDimension(id);
				} catch (Exception e) {
					// We ignore this error.
					Carpetbag.getLogger().info("        Could not unregister dimension: " + id);
				}
			} else {
				Carpetbag.getLogger().info("    Already unregistered! Dimension: " + id);
			}
		}
	}

	public void save(World world) {
		world.getMapStorage().setData(DIMMANAGER_NAME, this);
		markDirty();

		syncDimInfoToClients(world);
	}

	public void reclaimId(int id) {
		reclaimedIds.add(id);
	}

	/**
	 * Freeze a dimension: avoid ticking all tile entities and remove all
	 * active entities (they are still there but will not do anything).
	 * Entities that are within range of a player having a PFG will be kept
	 * active (but not tile entities).
	 */
	public static void freezeDimension(World world) {
		// TODO: Freeze dimension or delete the method
	}

	public static float squaredDistance(BlockPos p1, BlockPos c) {
		return ((c.getX() - p1.getX()) * (c.getX() - p1.getX()) + (c.getY() - p1.getY()) * (c.getY() - p1.getY()) + (c.getZ() - p1.getZ()) * (c.getZ() - p1.getZ()));
	}

	public static void unfreezeDimension(World world) {
		WorldServer worldServer = (WorldServer) world;
		for (Chunk chunk : worldServer.getChunkProvider().getLoadedChunks()) {
			unfreezeChunk(chunk);
		}
	}

	public static void unfreezeChunk(Chunk chunk) {
		chunk.getWorld().addTileEntities(chunk.getTileEntityMap().values());
		for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists()) {
			chunk.getWorld().loadedEntityList.addAll(entityList);
		}
	}

	public void syncDimInfoToClients(World world) {
		// TODO: is needed?
		if (!world.isRemote) {
			// Sync to clients.
			Carpetbag.getLogger().info("Sync dimension info to clients!");
			//RFToolsDimMessages.INSTANCE.sendToAll(new PacketSyncDimensionInfo(dimensions, dimensionInformation));
		}
	}

	// TODO: Is needed?
	/*public DimensionSyncPacket makeDimensionSyncPacket() {
		return new DimensionSyncPacket(dimensions, dimensionInformation);
	}*/

	public Map<Integer, DimensionDescriptor> getDimensions() {
		return dimensions;
	}

	public void registerDimensions() {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<Integer, DimensionDescriptor> me : dimensions.entrySet()) {
			int id = me.getKey();
			builder.append(id);
			builder.append(' ');
			registerDimensionToServerAndClient(id);
		}
		Carpetbag.getLogger().info("Registering Carpetbag dungeon dimensions: " + builder.toString());
	}

	private void registerDimensionToServerAndClient(int id) {
		if (!DimensionManager.isDimensionRegistered(id)) {
			DimensionManager.registerDimension(id, ModDimensions.dungeonType);
		}
		if(DimensionManager.getWorld(id) == null) {
			File chunkDir = new File(DimensionManager.getCurrentSaveRootDirectory(), DimensionManager.createProviderFor(id).getSaveFolder());
			if(ForgeChunkManager.savedWorldHasForcedChunkTickets(chunkDir)) {
				DimensionManager.initDimension(id);
			}
		}
		// TODO: This might actually be needed, fuck
		//RFToolsDimMessages.INSTANCE.sendToAll(new PacketRegisterDimensions(id));
	}

	public static DungeonManager getDimensionManagerClient() {
		if (clientInstance == null) {
			clientInstance = new DungeonManager(DIMMANAGER_NAME);
		}
		return clientInstance;
	}

	public static DungeonManager getDimensionManagerClientNullable(World world) {
		return clientInstance;
	}

	public static DungeonManager getDimensionManager(World world) {
		if (instance != null) {
			return instance;
		}
		instance = (DungeonManager) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(DungeonManager.class, DIMMANAGER_NAME);
		if (instance == null) {
			instance = new DungeonManager(DIMMANAGER_NAME);
		}
		return instance;
	}

	public DimensionDescriptor getDimensionDescriptor(int id) {
		return dimensions.get(id);
	}

	public Integer getDimensionID(DimensionDescriptor descriptor) {
		return dimensionToID.get(descriptor);
	}

	public DimensionInformation getDimensionInformation(int id) {
		return dimensionInformation.get(id);
	}

	/**
	 * Get a world for a dimension, possibly loading it from the configuration manager.
	 */
	public static World getWorldForDimension(World world, int id) {
		World w = DimensionManager.getWorld(id);
		if (w == null) {
			w = Objects.requireNonNull(world.getMinecraftServer()).getWorld(id);
		}
		return w;
	}

	public void removeDimension(int id) {
		DimensionDescriptor descriptor = dimensions.get(id);
		dimensions.remove(id);
		dimensionToID.remove(descriptor);
		dimensionInformation.remove(id);
		if (DimensionManager.isDimensionRegistered(id)) {
			DimensionManager.unregisterDimension(id);
		}
	}

	public void recoverDimension(World world, int id, DimensionDescriptor descriptor, String name) {
		if (!DimensionManager.isDimensionRegistered(id)) {
			registerDimensionToServerAndClient(id);
		}

		DimensionInformation dimensionInfo = new DimensionInformation(name, descriptor, world);

		dimensions.put(id, descriptor);
		dimensionToID.put(descriptor, id);
		dimensionInformation.put(id, dimensionInfo);

		save(world);
		touchSpawnChunk(world, id);
	}

	public int createNewDimension(World world, DimensionDescriptor descriptor, String name) {
		int id = 0;
		while (!reclaimedIds.isEmpty()) {
			int rid = reclaimedIds.iterator().next();
			reclaimedIds.remove(rid);
			if (!DimensionManager.isDimensionRegistered(rid)) {
				id = rid;
				break;
			}
		}
		if (id == 0) {
			id = DimensionManager.getNextFreeDimId();
		}

		registerDimensionToServerAndClient(id);
		Carpetbag.getLogger().info("id = " + id + " for " + name + ", descriptor = " + descriptor.getDescriptionString());


		dimensions.put(id, descriptor);
		dimensionToID.put(descriptor, id);


		try {
			DimensionInformation dimensionInfo = new DimensionInformation(name, descriptor, world);
			dimensionInformation.put(id, dimensionInfo);
		} catch (Exception e) {
			Carpetbag.getLogger().error("Something went wrong during creation of the dimension!");
			e.printStackTrace();
		}

		save(world);

		touchSpawnChunk(world, id);
		return id;
	}

	private void touchSpawnChunk(World world, int id) {
		// Make sure world generation kicks in for at least one chunk so that our matter receiver
		// is generated and registered.
		WorldServer worldServerForDimension = Objects.requireNonNull(world.getMinecraftServer()).getWorld(id);
		ChunkProviderServer providerServer = worldServerForDimension.getChunkProvider();
		if (!providerServer.chunkExists(0, 0)) {
			try {
				providerServer.provideChunk(0, 0);
				providerServer.chunkGenerator.populate(0, 0);
			} catch (Exception e) {
				Carpetbag.getLogger().error("Something went wrong during creation of the dimension!");
				e.printStackTrace();
				// We catch this exception to make sure our dimension tab is at least ok.
			}
		}
	}


	// -----------------------------------------------
	// -----------------------------------------------
	// -----------------------------------------------



	public void createDungeon(List<EntityPlayer> players, World world, DimensionDescriptor descriptor) {
		// TODO: actually create the dungeon lmao
		int id = createNewDimension(world, descriptor, "dungeon");
		for (EntityPlayer p : players)
			sendPlayerToActiveDungeon(id, p);
	}

	// TODO: Throw custom error instead of retuning a string
	public String sendPlayerToActiveDungeon(int id, EntityPlayer player) {
		// TODO: actually load the dungeon or smth?
		if (!getDimensionInformation(id).isActive() && !dimensions.containsKey(id)) {
			return "Dungeon is inactive";
		}
		//.loadData(dungeon.data)
		//dest.getChunkProvider().
		player.dismountRidingEntity();
		if (player.isPlayerSleeping()) player.wakeUpPlayer(true, true, false);

		player.changeDimension(id, (world, entity, yaw) -> entity.setWorld(world));

		return null;
	}

	public void scheduleDestroyDungeon(int id) {
		Carpetbag.SCHEDULER.addScheduledTask(() -> destroyDungeon(id), 30000);
	}

	private void destroyDungeon(int id) {
		// TODO: more?
		removeDimension(id);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		dimensions.clear();
		dimensionToID.clear();
		dimensionInformation.clear();
		reclaimedIds.clear();
		NBTTagList lst = tagCompound.getTagList("dimensions", Constants.NBT.TAG_COMPOUND);
		for (int i = 0 ; i < lst.tagCount() ; i++) {
			NBTTagCompound tc = lst.getCompoundTagAt(i);
			int id = tc.getInteger("id");
			DimensionDescriptor descriptor = new DimensionDescriptor(tc);
			dimensions.put(id, descriptor);
			dimensionToID.put(descriptor, id);

			DimensionInformation dimensionInfo = new DimensionInformation(descriptor, tc);
			dimensionInformation.put(id, dimensionInfo);
		}

		int[] lstIds = tagCompound.getIntArray("reclaimedIds");
		for (int id : lstIds) {
			reclaimedIds.add(id);
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList lst = new NBTTagList();
		for (Map.Entry<Integer,DimensionDescriptor> me : dimensions.entrySet()) {
			NBTTagCompound tc = new NBTTagCompound();

			Integer id = me.getKey();
			tc.setInteger("id", id);
			me.getValue().writeToNBT(tc);
			DimensionInformation dimensionInfo = dimensionInformation.get(id);
			dimensionInfo.writeToNBT(tc);

			lst.appendTag(tc);
		}
		tagCompound.setTag("dimensions", lst);

		List<Integer> ids = new ArrayList<>(reclaimedIds);
		int[] lstIds = new int[ids.size()];
		for (int i = 0 ; i < ids.size() ; i++) {
			lstIds[i] = ids.get(i);
		}
		tagCompound.setIntArray("reclaimedIds", lstIds);
		return tagCompound;
	}
}
