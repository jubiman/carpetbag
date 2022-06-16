package us.jusybiberman.carpetbag.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import us.jusybiberman.carpetbag.Carpetbag;

public abstract class SkillBase {
	public static int addExp(EntityPlayer player, int toAdd, String skill) {
		int currExp = player.getEntityData().getCompoundTag("CarpetbagData").getCompoundTag("SkillMining").getInteger("exp");

		if(player.getEntityData().getCompoundTag("CarpetbagData").isEmpty()) {
			// Shouldn't be but ok...
			// TODO: Maybe throw an error?
			// TODO: Just do some testing for now
			return -1;
		}

		player.getEntityData().getCompoundTag("CarpetbagData").getCompoundTag(skill).setInteger("exp", currExp + toAdd);

		return currExp + toAdd;
	}

	public static void removeExp(EntityPlayer sender, int amount, String skill_type) {
	}

	public static void setExp(EntityPlayer sender, int amount, String skill_type) {
	}
}
