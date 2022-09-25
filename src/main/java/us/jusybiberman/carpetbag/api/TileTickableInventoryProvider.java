package us.jusybiberman.carpetbag.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.storage.PlayerSideItemStackHandler;

public abstract class TileTickableInventoryProvider<T extends TileEntity> extends TileInventoryProvider<T> implements ITickable {
	public TileTickableInventoryProvider(T t, EntityPlayer p, int size) {
		super(t, p, size);
	}
	@Override
	public abstract void update();
}
