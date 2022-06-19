package us.jusybiberman.carpetbag.stats;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class CPBStatBase {
	private int level;

	public CPBStatBase() {
		level = 0;
	}

	public CPBStatBase(int lvl) {
		level = lvl;
	}

	public void addLevel(int amount) {
		level += amount;
	}

	public int getLevel() {
		return level;
	}

	public NBTTagCompound save() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("level", level);
		return tag;
	}
}
