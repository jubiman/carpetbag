package us.jusybiberman.carpetbag.events;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinEvent {
	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.player.getEntityData().getCompoundTag("CarpetbagData").isEmpty()) {
			// Init mod data
			NBTTagCompound playerData = event.player.getEntityData();
			playerData.setTag("CarpetbagData", new NBTTagCompound());
			NBTTagCompound carpetbagData = playerData.getCompoundTag("CarpetbagData");

			// Init skill data
			carpetbagData.setTag("SkillMining", new NBTTagCompound());
			carpetbagData.getCompoundTag("SkillMining").setInteger("exp", 0);
			carpetbagData.getCompoundTag("SkillMining").setInteger("level", 0);

			carpetbagData.setTag("SkillForaging", new NBTTagCompound());
			carpetbagData.getCompoundTag("SkillForaging").setInteger("exp", 0);
			carpetbagData.getCompoundTag("SkillForaging").setInteger("level", 0);

			carpetbagData.setTag("SkillFishing", new NBTTagCompound());
			carpetbagData.getCompoundTag("SkillFishing").setInteger("exp", 0);
			carpetbagData.getCompoundTag("SkillFishing").setInteger("level", 0);

			carpetbagData.setTag("SkillSmithing", new NBTTagCompound());
			carpetbagData.getCompoundTag("SkillSmithing").setInteger("exp", 0);
			carpetbagData.getCompoundTag("SkillSmithing").setInteger("level", 0);

			carpetbagData.setTag("SkillCombat", new NBTTagCompound());
			carpetbagData.getCompoundTag("SkillCombat").setInteger("exp", 0);
			carpetbagData.getCompoundTag("SkillCombat").setInteger("level", 0);


			// Init stat data
			carpetbagData.setTag("StatStr", new NBTTagCompound());
			carpetbagData.getCompoundTag("StatStr").setInteger("level", 0);

			carpetbagData.setTag("StatInt", new NBTTagCompound());
			carpetbagData.getCompoundTag("StatInt").setInteger("level", 0);

			carpetbagData.setTag("StatDex", new NBTTagCompound());
			carpetbagData.getCompoundTag("StatDex").setInteger("level", 0);
		}
	}
}
