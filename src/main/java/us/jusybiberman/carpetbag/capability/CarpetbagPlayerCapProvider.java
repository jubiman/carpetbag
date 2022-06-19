package us.jusybiberman.carpetbag.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;

import javax.annotation.Nonnull;

public class CarpetbagPlayerCapProvider implements ICapabilitySerializable<NBTBase> {
	private final ICarpetbagPlayer capInstance = CPBCapabilityManager.getCarpetbagPlayerCap().getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		return capability == CPBCapabilityManager.getCarpetbagPlayerCap();
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		Capability<ICarpetbagPlayer> playerCap = CPBCapabilityManager.getCarpetbagPlayerCap();
		return capability == playerCap ? playerCap.cast(capInstance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		Capability<ICarpetbagPlayer> playerCap = CPBCapabilityManager.getCarpetbagPlayerCap();

		return playerCap.getStorage().writeNBT(playerCap, capInstance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capability<ICarpetbagPlayer> playerCap = CPBCapabilityManager.getCarpetbagPlayerCap();
		playerCap.getStorage().readNBT(playerCap, capInstance, null, nbt);
	}
}
