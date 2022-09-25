package us.jusybiberman.carpetbag.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.api.TileInventoryProvider;
import us.jusybiberman.carpetbag.block.tatara.ProviderTatara;
import us.jusybiberman.carpetbag.storage.PlayerSideItemStackHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public abstract class TileEntityWithProviders<T extends TileInventoryProvider, R> extends TileEntityBase {
	protected final HashMap<UUID, T> providers = new HashMap<>();
	protected NBTTagCompound compound = new NBTTagCompound();

	private final Class<T> clazz;
	private final Class<R> tile;

	public TileEntityWithProviders(Class<T> impl, Class<R> t) {
		super();
		clazz = impl;
		tile = t;
	}
	public void createProvider(EntityPlayer player) {
		T p;
		try {
			p = clazz.getConstructor(new Class<?>[]{tile, EntityPlayer.class}).newInstance(tile, player);
			//p = (T) clazz.getConstructors()[0].newInstance(tile, player);
			Carpetbag.getLogger().debug(p);
			Carpetbag.getLogger().debug("YEEEES");
		} catch (Exception e) {
			Carpetbag.getLogger().error("Failed to create new provider for " + player.getName() + ". " + e.getMessage());
			for(StackTraceElement line : e.getStackTrace())
				Carpetbag.getLogger().error(line);
			return;
		}
		if (compound.hasKey(player.getUniqueID().toString())) {
			p.readDataFromNBT(compound.getCompoundTag(player.getUniqueID().toString()));
			providers.put(player.getUniqueID(), p);
			return;
		}
		providers.put(player.getUniqueID(), p);
	}

	public T getProvider(EntityPlayer player) {
		if (!providers.containsKey(player.getUniqueID()))
			createProvider(player);
		return providers.get(player.getUniqueID());
	}

	public PlayerSideItemStackHandler getInventory(EntityPlayer player) {
		if (!providers.containsKey(player.getUniqueID()))
			createProvider(player);
		return providers.get(player.getUniqueID()).inventory;
	}
}
