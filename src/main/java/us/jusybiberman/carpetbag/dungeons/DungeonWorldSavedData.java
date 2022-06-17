package us.jusybiberman.carpetbag.dungeons;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;


public class DungeonWorldSavedData extends WorldSavedData {
	public DungeonWorldSavedData(String name) {
		super(name);
	}

	public static DungeonWorldSavedData get(World world) {
		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		/*MapStorage storage = IS_GLOBAL == 1 ? world.getMapStorage() : world.getPerWorldStorage();
		DungeonWorldSavedData instance = (DungeonWorldSavedData) storage.getOrLoadData(DungeonWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new DungeonWorldSavedData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;*/
		return null;
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return null;
	}
}
