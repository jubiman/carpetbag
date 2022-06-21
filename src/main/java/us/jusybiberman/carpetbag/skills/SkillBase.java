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
		threshold = nextThreshold();
	}

	public SkillBase(long xp, long thr) {
		exp = xp;
		threshold = thr;
	}

	public boolean addExp(long amount) {
		exp += amount;
		return levelUp();
	}

	public boolean removeExp(long amount){
		exp -= amount;
		return levelUp();
	}

	public boolean setExp(long amount) {
		exp = amount;
		return levelUp();
	}

	public boolean levelUp() {
		if(exp > threshold) {
			threshold = nextThreshold();
			return true;
		}
		return false;
		// TODO: unlock items/rewards and add messages?
	}

	public int getLevel() {
		// TODO: find out how to get level from exp
		return (int) Math.floor((250 + Math.sqrt(62500 + 1000 * exp)) / 500);
	}

	public long getExp() {
		return exp;
	}

	private long nextThreshold() {
		// TODO: decide whether to take this formula or a different one
		// http://howtomakeanrpg.com/a/how-to-make-an-rpg-levels.html
		int x = getLevel() + 1;
		return 250 * ((long) x * x) - (250L * x);
	}

	public NBTTagCompound save() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("exp", exp);
		tag.setLong("threshold", threshold);
		return tag;
	}
}
