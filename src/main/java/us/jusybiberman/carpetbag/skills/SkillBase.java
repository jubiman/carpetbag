package us.jusybiberman.carpetbag.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import us.jusybiberman.carpetbag.Carpetbag;

public class SkillBase {
	// TODO: save exp as total exp? then maybe make it a long? or prestiges?
	private int exp;
	private int threshold;

	public SkillBase() {
		exp = 0;
		threshold = 10;
	}

	public SkillBase(int xp, int thr) {
		exp = xp;
		threshold = thr;
	}

	@Deprecated
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

	public void addExp(int amount) {
		exp += amount;
		levelUp();
	}

	public void removeExp(int amount){
		exp -= amount;
		levelUp();
	}

	public void setExp(int amount) {
		exp = amount;
		levelUp();
	}

	public void levelUp() {
		while(exp > threshold)
			threshold = nextThreshold();
	}

	public int getLevel() {
		// TODO: find out how to get level from exp
		return (int) Math.floor((250 + Math.sqrt(62500 + 1000 * exp)) / 500);
	}

	public int getExp() {
		return exp;
	}

	private int nextThreshold() {
		// TODO: decide whether to take this formula or a different one
		// http://howtomakeanrpg.com/a/how-to-make-an-rpg-levels.html
		int x = getLevel();
		return 250 * (x * x) - (250 * x);
	}

	public NBTTagCompound save() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("exp", exp);
		tag.setInteger("threshold", threshold);
		return tag;
	}
}
