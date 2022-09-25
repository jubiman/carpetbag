package us.jusybiberman.carpetbag.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.api.TileInventoryProvider;
import us.jusybiberman.carpetbag.api.TileTickableInventoryProvider;
import us.jusybiberman.carpetbag.storage.PlayerSideItemStackHandler;

import java.util.HashMap;
import java.util.UUID;

public abstract class TileEntityWithTickableProviders<T extends TileTickableInventoryProvider, R> extends TileEntityWithProviders<T, R> implements ITickable {
	public TileEntityWithTickableProviders(Class<T> impl, Class<R> tile) {
		super(impl, tile);
	}

	@Override
	public void update() {
		for (UUID k : providers.keySet())
			providers.get(k).update();
	}
}
