package us.jusybiberman.carpetbag.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.api.capability.IStatStorage;
import us.jusybiberman.carpetbag.api.capability.SerializableInnerCap;
import us.jusybiberman.carpetbag.stats.CPBStatBase;

import java.util.HashMap;
import java.util.Objects;

public class StatStorage extends SerializableInnerCap<NBTBase, StatStorage> implements IStatStorage {
	private final HashMap<String, CPBStatBase> stats = new HashMap<>();

	public StatStorage() {
		stats.put("mining", new CPBStatBase());
		stats.put("enchanting", new CPBStatBase());
		stats.put("smithing", new CPBStatBase());
		stats.put("foraging", new CPBStatBase());
		stats.put("combat", new CPBStatBase());
		stats.put("fishing", new CPBStatBase());
	}

	@Override
	public CPBStatBase getStat(String name) {
		return stats.get(name);
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		for (String k : stats.keySet())
			tag.setTag(k, stats.get(k).save());

		compound.setTag("stats", tag);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		if (nbt instanceof NBTTagCompound)
			readFromNBT(((NBTTagCompound) nbt));
		else Carpetbag.getLogger().error("WTF nbt isn't NBTTagCompound (in StatStorage.java:51)");
	}

	private void readFromNBT(NBTTagCompound tag) {
		stats.clear();
		stats.put("str", new CPBStatBase(tag.getCompoundTag("str").getInteger("level")));
		stats.put("int", new CPBStatBase(tag.getCompoundTag("int").getInteger("level")));
		stats.put("dex", new CPBStatBase(tag.getCompoundTag("dex").getInteger("level")));
	}

	@Override
	public void writeToBuffer(PacketBuffer buffer) {
		NBTTagCompound comp = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		for (String k : stats.keySet())
			tag.setTag(k, stats.get(k).save());

		comp.setTag("skill", tag);
		ByteBufUtils.writeTag(buffer, comp);
	}

	@Override
	public void readFromBuffer(PacketBuffer buffer) {
		NBTTagCompound tag = Objects.requireNonNull(ByteBufUtils.readTag(buffer)).getCompoundTag("stats");
		stats.clear();
		stats.put("str", new CPBStatBase(tag.getCompoundTag("str").getInteger("level")));
		stats.put("int", new CPBStatBase(tag.getCompoundTag("int").getInteger("level")));
		stats.put("dex", new CPBStatBase(tag.getCompoundTag("dex").getInteger("level")));
	}
}
