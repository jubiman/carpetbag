package us.jusybiberman.carpetbag.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import us.jusybiberman.carpetbag.Carpetbag;

public class SkillBase {
	// TODO: save exp as total exp? then maybe make it a long? or prestiges?
	private long exp;
	private long threshold;

	public SkillBase() {
		exp = 0;
		threshold = 10;
	}

	public SkillBase(long xp, long thr) {
		exp = xp;
		threshold = thr;
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

	public long getExp() {
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
		tag.setLong("exp", exp);
		tag.setLong("threshold", threshold);
		return tag;
	}
}
