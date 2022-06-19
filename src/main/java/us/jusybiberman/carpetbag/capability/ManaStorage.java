package us.jusybiberman.carpetbag.capability;

import us.jusybiberman.carpetbag.api.capability.IManaStorage;
import us.jusybiberman.carpetbag.api.capability.SerializableInnerCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.PacketBuffer;

public class ManaStorage extends SerializableInnerCap<NBTBase, ManaStorage> implements IManaStorage {
	private int maxValue = 100;
	private int manaValue = 0;
	private int regenCooldown = 0;
	private int regenPerTick = 10;
	private boolean isBeingUsed = false;


	public void setMaxValue(int value) {
		maxValue = value;
	}

	public void setRegenPerTick(int value) {
		regenPerTick = value;
	}

	@Override
	public boolean useEssence(int points) {
		if (manaValue < points)
			return false;
		manaValue -= points;
		return true;
	}

	@Override
	public void addEssence(int points) {
		manaValue += points;

		coerceEssence(this);
	}

	@Override
	public int getEssenceValue() {
		return manaValue;
	}

	@Override
	public int getMaxValue() {
		return maxValue;
	}

	@Override
	public boolean isFull() {
		return getEssenceValue() == getMaxValue();
	}

	@Override
	public boolean isBeingUsed() {
		return isBeingUsed;
	}

	@Override
	public void onTick() {
		if (regenCooldown-- <= 0) regenCooldown = 20;
		if (regenCooldown >= 20) regen();

		if (regenCooldown < regenPerTick && manaValue < maxValue) {
			isBeingUsed = true;
		}
		if (isFull()) {
			isBeingUsed = false;
		}
	}

	@Override
	public void regen() {
		addEssence(regenPerTick);
	}

	private static void coerceEssence(ManaStorage manaBar) {
		manaBar.manaValue = Math.min(manaBar.getEssenceValue(), manaBar.getMaxValue());
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("regen_cooldown", regenCooldown);
		compound.setInteger("mana_value", manaValue);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		if (nbt instanceof NBTTagInt) {
			readFromOldNBT((NBTTagInt) nbt);
		} else {
			readFromNBT(((NBTTagCompound) nbt));
		}
	}

	private void readFromNBT(NBTTagCompound tag) {
		regenCooldown = tag.getInteger("regen_cooldown");
		manaValue = tag.getInteger("mana_value");

		coerceEssence(this);
	}

	private void readFromOldNBT(NBTTagInt tag) {
		manaValue = tag.getInt();
	}

	@Override
	public void writeToBuffer(PacketBuffer buffer) {
		buffer.writeInt(regenCooldown);
		buffer.writeInt(manaValue);
	}

	@Override
	public void readFromBuffer(PacketBuffer buffer) {
		regenCooldown = buffer.readInt();
		manaValue = buffer.readInt();
	}
}
