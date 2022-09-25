package us.jusybiberman.carpetbag.block.kanatoko;

import net.minecraft.nbt.NBTTagCompound;
import us.jusybiberman.carpetbag.tileentity.TileEntityWithProviders;

public class TileEntityKanatoko extends TileEntityWithProviders<ProviderKanatoko, TileEntityKanatoko> {
	public TileEntityKanatoko() {
		super(ProviderKanatoko.class, TileEntityKanatoko.class);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound) {

	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound) {

	}
}
