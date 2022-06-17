package us.jusybiberman.carpetbag.dungeons;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class DimensionStorage extends AbstractWorldData<DimensionStorage> {

	private static final String DIMSTORAGE_NAME = "CarpetbagDungeonStorage";

	private static DimensionStorage clientInstance = null;

	public DimensionStorage(String name) {
		super(name);
	}

	@Override
	public void clear() {

	}

	public static DimensionStorage getDimensionStorage(World world) {
		if (world.isRemote) {
			if (clientInstance == null) {
				clientInstance = new DimensionStorage(DIMSTORAGE_NAME);
			}
			return clientInstance;
		}
		return getData(world, DimensionStorage.class, DIMSTORAGE_NAME);
	}

	public void removeDimension(int id) {

	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		NBTTagList lst = tagCompound.getTagList("dimensions", Constants.NBT.TAG_COMPOUND);
		for (int i = 0 ; i < lst.tagCount() ; i++) {
			NBTTagCompound tc = lst.getCompoundTagAt(i);
			int id = tc.getInteger("id");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList lst = new NBTTagList();
		tagCompound.setTag("dimensions", lst);
		return tagCompound;
	}
}