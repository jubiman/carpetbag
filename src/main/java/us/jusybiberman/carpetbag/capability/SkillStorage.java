package us.jusybiberman.carpetbag.capability;

import javafx.util.Pair;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import us.jusybiberman.carpetbag.Carpetbag;
import us.jusybiberman.carpetbag.api.capability.ISkillStorage;
import us.jusybiberman.carpetbag.api.capability.SerializableInnerCap;
import us.jusybiberman.carpetbag.skills.SkillBase;

import java.util.HashMap;
import java.util.Objects;

public class SkillStorage extends SerializableInnerCap<NBTBase, SkillStorage> implements ISkillStorage {
	private final HashMap<String, SkillBase> skills = new HashMap<>();

	public SkillStorage() {
		skills.put("mining", new SkillBase());
		skills.put("enchanting", new SkillBase());
		skills.put("smithing", new SkillBase());
		skills.put("foraging", new SkillBase());
		skills.put("combat", new SkillBase());
		skills.put("fishing", new SkillBase());
	}

	@Override
	public SkillBase getSkill(String name) {
		return skills.get(name);
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		for (String k : skills.keySet())
			tag.setTag(k, skills.get(k).save());

		compound.setTag("skills", tag);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		if (nbt instanceof NBTTagCompound)
			readFromNBT(((NBTTagCompound) nbt));
		else Carpetbag.getLogger().error("WTF nbt isn't NBTTagCompound (in SkillStorage.java:51)");
	}

	private void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("skills")) {
			skills.clear();
			skills.put("mining", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("mining").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("mining").getLong("threshold")));
			skills.put("enchanting", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("enchanting").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("enchanting").getLong("threshold")));
			skills.put("smithing", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("smithing").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("smithing").getLong("threshold")));
			skills.put("foraging", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("foraging").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("foraging").getLong("threshold")));
			skills.put("combat", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("combat").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("combat").getLong("threshold")));
			skills.put("fishing", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("fishing").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("fishing").getLong("threshold")));
		}
	}

	@Override
	public void writeToBuffer(PacketBuffer buffer) {
		NBTTagCompound comp = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		for (String k : skills.keySet())
			tag.setTag(k, skills.get(k).save());

		comp.setTag("skills", tag);
		ByteBufUtils.writeTag(buffer, comp);
	}

	@Override
	public void readFromBuffer(PacketBuffer buffer) {
		NBTTagCompound tag = Objects.requireNonNull(ByteBufUtils.readTag(buffer));
		if (tag.hasKey("skills")) {
			skills.clear();
			skills.put("mining", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("mining").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("mining").getLong("threshold")));
			skills.put("enchanting", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("enchanting").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("enchanting").getLong("threshold")));
			skills.put("smithing", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("smithing").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("smithing").getLong("threshold")));
			skills.put("foraging", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("foraging").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("foraging").getLong("threshold")));
			skills.put("combat", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("combat").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("combat").getLong("threshold")));
			skills.put("fishing", new SkillBase(tag.getCompoundTag("skills").getCompoundTag("fishing").getLong("exp"), tag.getCompoundTag("skills").getCompoundTag("fishing").getLong("threshold")));
		}
	}
}
