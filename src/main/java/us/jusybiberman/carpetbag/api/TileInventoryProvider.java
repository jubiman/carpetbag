package us.jusybiberman.carpetbag.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.storage.PlayerSideItemStackHandler;

public abstract class TileInventoryProvider<T extends TileEntity> {

	public PlayerSideItemStackHandler inventory;

	protected final T tile;
	protected final ICarpetbagPlayer player;

	public TileInventoryProvider(T t, EntityPlayer p, int size) {
		tile = t;
		player = CPBCapabilityManager.asCarpetbagPlayer(p);
		inventory = new PlayerSideItemStackHandler(true, size); // TODO: check if aw needs to be variable
	}

	public void writeDataToNBT(NBTTagCompound compound) {
		compound.merge(inventory.serializeNBT());
	}

	public void readDataFromNBT(NBTTagCompound compound) {
		inventory = new PlayerSideItemStackHandler(true, compound.getInteger("Size"));
		inventory.deserializeNBT(compound);
	}

	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return tile.hasCapability(capability, facing);
	}

	public <R> R getCapability(Capability<R> capability, EnumFacing facing) {
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (R) inventory;
		return tile.getCapability(capability, facing);
	}
}
