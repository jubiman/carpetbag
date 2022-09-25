package us.jusybiberman.carpetbag.block.kanatoko;

import net.minecraft.entity.player.EntityPlayer;
import us.jusybiberman.carpetbag.api.TileInventoryProvider;

public class ProviderKanatoko extends TileInventoryProvider<TileEntityKanatoko> {
	public ProviderKanatoko(TileEntityKanatoko tileEntityKanatoko, EntityPlayer p) {
		super(tileEntityKanatoko, p, 10);
	}
}
